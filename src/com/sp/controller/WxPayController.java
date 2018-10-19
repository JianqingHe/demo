package com.sp.controller;

import com.github.binarywang.utils.qrcode.QrcodeUtils;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.sp.api.WxPayApi;
import com.sp.config.WxConfig;
import com.sp.result.ResultMap;
import com.sp.support.AgentUtils;
import com.sp.support.FileUtils;
import com.sp.support.IpUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;

/**
 * 微信支付
 *
 * @author hejq
 * @date 2018-10-10 10:32
 */
@RequestMapping("/wx")
@RestController
public class WxPayController extends BaseController {

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    protected WxPayApi wxPayApi;

    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResultMap createWxPay(@RequestParam String code, @RequestParam String name) throws UnsupportedEncodingException {
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = createWxPayUnifiedOrderRequest(request, code, name, WxPayConstants.TradeType.NATIVE);
        ResultMap resultMap = unifiedOrder(wxPayUnifiedOrderRequest, null);
        return resultMap;
    }

    /**
     * 通过统一下单接口发起请求，获得prepay_id（预支付交易会话标识），这个标示是微信提交支付的关键数据
     *
     * @param wxPayUnifiedOrderRequest
     * @param currentUrl
     * @return
     */
    private ResultMap unifiedOrder(WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest, String currentUrl) throws UnsupportedEncodingException {
        WxPayUnifiedOrderResult wxPayUnifiedOrderResult;
        ModelMap map = new ModelMap();
        try {
            wxPayUnifiedOrderResult = wxPayApi.unifiedOrder(wxPayUnifiedOrderRequest);

            if (WxPayConstants.TradeType.MWEB.equals(wxPayUnifiedOrderRequest.getTradeType())) {
                /*H5支付开发步骤：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=15_4*/
                String mwebUrl;
                if (currentUrl != null) {
                    logger.error("当前用户所在页面url:" + currentUrl);
                    mwebUrl = wxPayUnifiedOrderResult.getMwebUrl() + "&redirect_url=" + URLEncoder.encode(currentUrl + "/" + wxPayUnifiedOrderRequest.getOutTradeNo(),"UTF-8");;
                } else {
                    mwebUrl = wxPayUnifiedOrderResult.getMwebUrl();
                }
                map.put("mwebUrl", mwebUrl);
            } else if (WxPayConstants.TradeType.NATIVE.equals(wxPayUnifiedOrderRequest.getTradeType())) {
                //扫码支付
                byte[] bytes = QrcodeUtils.createQrcode(wxPayUnifiedOrderResult.getCodeURL(), 400, null);
                String path = "E:\\img\\";
                String name = "qrcode" + Math.round(5);
                String qrCodeUrl = FileUtils.saveFile(bytes, name, path, FileUtils.Image.JPG);
                String outTradeNo = wxPayUnifiedOrderRequest.getOutTradeNo();
                map.put("outTradeNo", outTradeNo);
                //保存图片地址
                map.put("qrCodeUrl", qrCodeUrl);
            } else if (WxPayConstants.TradeType.JSAPI.equals(wxPayUnifiedOrderRequest.getTradeType())) {
                //公众号支付
                String prepayId = wxPayUnifiedOrderResult.getPrepayId();
                map.put("prepayId", prepayId);
            } else if (WxPayConstants.TradeType.APP.equals(wxPayUnifiedOrderRequest.getTradeType())) {
                //APP支付
                String prepayId = wxPayUnifiedOrderResult.getPrepayId();
                map.put("prepayId", prepayId);
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            map.put("status", e.getReturnCode());
            map.put("message", e.getReturnMsg());
        }
        return ResultMap.success(map);
    }

    private WxPayUnifiedOrderRequest createWxPayUnifiedOrderRequest(HttpServletRequest request, String code, String name, String tradeType) {
        WxConfig config = new WxConfig();
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = config.convert();
        WxPayConfig payConfig = wxPayApi.getConfig();
        payConfig.setAppId(WxConfig.APPID);
        payConfig.setMchId(WxConfig.MCHID);
        payConfig.setMchKey(WxConfig.MCHKEY);
        wxPayApi.setConfig(payConfig);
        //否 签名类型，目前支持HMAC-SHA256和MD5，默认为MD5
        //wxPayUnifiedOrderRequest.setSignType(null);


        //商品描述交易字段格式根据不同的应用场景按照以下格式：
        // （1）PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
        // （2） 公众号——传入公众号名称-实际商品名称，例如：腾讯形象店- image-QQ公仔；
        // （3） H5——应用在浏览器网页上的场景，传入浏览器打开的移动网页的主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
        // （4） 线下门店——门店品牌名-城市分店名-实际商品名称，例如： image形象店-深圳腾大- QQ公仔）
        // （5） APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值。
        /*if (WxPayConstants.TradeType.MWEB.equals(tradeType)) {
            //H5——应用在浏览器网页上的场景，传入浏览器打开的移动网页的主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
            wxPayUnifiedOrderRequest.setBody("test-" + code);
        } else if (WxPayConstants.TradeType.JSAPI.equals(tradeType)) {
            //否是 trade_type=JSAPI，此参数必传，用户在子商户appid下的唯一标识。openid和sub_openid可以选传其中之一，如果选择传sub_openid,则必须传sub_appid。
            if (StringUtils.isEmpty(wxPayApi.getConfig().getSubAppId())) {
                wxPayUnifiedOrderRequest.setOpenid(null);
            } else {
                wxPayUnifiedOrderRequest.setSubOpenid(null);
            }

            //公众号——传入公众号名称-实际商品名称，例如：腾讯形象店- image-QQ公仔；
            wxPayUnifiedOrderRequest.setBody("test-" + code);
        } else if (WxPayConstants.TradeType.NATIVE.equals(tradeType)) {
            //PC网站——传入浏览器打开的网站主页title名-实际商品名称，例如：腾讯充值中心-QQ会员充值；
            wxPayUnifiedOrderRequest.setBody("test-" + name);
        } else if (WxPayConstants.TradeType.APP.equals(tradeType)) {
            //APP——需传入应用市场上的APP名字-实际商品名称，天天爱消除-游戏充值
            wxPayUnifiedOrderRequest.setBody("test-" + name);
        }*/

        //是 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
        wxPayUnifiedOrderRequest.setOutTradeNo(code);

        //wxPayUnifiedOrderRequest.setFeeType("CNY");//否 符合ISO 4217标准的三位字母代码，默认人民币：CNY
        //是 订单总金额，只能为整数
        wxPayUnifiedOrderRequest.setTotalFee(1);

        String spbillCreateIp = IpUtils.getIpFromRequest(request);
        if ("0:0:0:0:0:0:0:1".equals(spbillCreateIp)) {
            spbillCreateIp = "127.0.0.1";
        }
        logger.info("获取用户机器IP,方式一:IpUtils.getIpFromRequest(request)=" + spbillCreateIp);
        logger.info("获取用户机器IP,方式二:getIp(request)=" + AgentUtils.getIp(request));
        logger.info("获取用户机器IP,方式三:getRealIp(request)=" + AgentUtils.getIp(request));
        logger.info("获取用户机器IP,方式四:AgentUtils.getIp(request)=" + AgentUtils.getIp(request));
        //是 APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
        wxPayUnifiedOrderRequest.setSpbillCreateIp(AgentUtils.getIp(request));

        //wxPayUnifiedOrderRequest.setTimeStart(null);//否 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
        //wxPayUnifiedOrderRequest.setTimeExpire(null);//否 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
        //wxPayUnifiedOrderRequest.setGoodsTag(null);//否 订单优惠标记，代金券或立减优惠功能的参数

        //是 取值如下：JSAPI，NATIVE，APP
        wxPayUnifiedOrderRequest.setTradeType(tradeType);

        //否是 trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
        wxPayUnifiedOrderRequest.setProductId(String.valueOf(1));

        //wxPayUnifiedOrderRequest.setLimitPay(null);//否 指定支付方式 如：no_credit--指定不能使用信用卡支付
        //wxPayUnifiedOrderRequest.setSceneInfo(null);//否 该字段用于上报场景信息，目前支持上报实际门店信息。该字段为JSON对象数据，对象格式为{"store_info":{"id": "门店ID","name": "名称","area_code": "编码","address": "地址" }}

        return wxPayUnifiedOrderRequest;
    }
}
