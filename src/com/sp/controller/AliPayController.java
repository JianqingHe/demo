package com.sp.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.sp.api.AliPayApi;
import com.sp.config.AliPayConfig;
import com.sp.entity.AliPayModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.Map;

/**
 * 支付宝
 *
 * @author hejq
 * @date 2018-10-19 9:07
 */
@RequestMapping("/ali")
@RestController
public class AliPayController extends BaseController {

    private AlipayClient alipayClient =
            new DefaultAlipayClient(AliPayConfig.GATEWAY, AliPayConfig.APPID, AliPayConfig.RSA_PRIVATE_KEY,
                    AliPayConfig.FORMAT, AliPayConfig.CHARSET, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.SIGNTYPE);

    private AliPayModel payModel = new AliPayModel();

    /**
     * PC支付
     */
    @ResponseBody
    @RequestMapping(value = "/pcPay", method = RequestMethod.POST)
    public void pcPay() throws IOException {

        try {
            AlipayTradePagePayModel model = new AlipayTradePagePayModel();
            //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。该参数数值不接受小数点， 如 1.5h，可转换为 90m。
            model.setTimeoutExpress("30m");
            //model.setSellerId("");
            model.setOutTradeNo(payModel.getTradeNo());
            model.setProductCode("FAST_INSTANT_TRADE_PAY");
            model.setTotalAmount(payModel.getAmount());
            model.setSubject(payModel.getProjectName());
            model.setBody("PC网页支付");
            //model.setPassbackParams(URLEncoder.encode("附加消息", "UTF-8"));
            /*PC扫码支付的方式，支持前置模式和跳转模式。
                前置模式是将二维码前置到商户
                的订单确认页的模式。需要商户在
                自己的页面中以 iframe 方式请求
                支付宝页面。具体分为以下几种：
                0：订单码-简约前置模式，对应 iframe 宽度不能小于600px，高度不能小于300px；
                1：订单码-前置模式，对应iframe 宽度不能小于 300px，高度不能小于600px；
                3：订单码-迷你前置模式，对应 iframe 宽度不能小于 75px，高度不能小于75px；
                4：订单码-可定义宽度的嵌入式二维码，商户可根据需要设定二维码的大小。

                跳转模式下，用户的扫码界面是由支付宝生成的，不在商户的域名下。
                2：订单码-跳转模式
            */
            //model.setQrPayMode("2");//跳转模式
            //model.setRequestFromUrl("");


            AliPayApi.tradePage(response, model, alipayClient, AliPayConfig.RETURN_URL, AliPayConfig.NOTIFY_URL);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("创建订单失败，请稍后再试！");
        }
    }

    /**
     * 手机 wap支付
     */
    @ResponseBody
    @RequestMapping(value = "/wapPay", method = RequestMethod.POST)
    public void wapPay() throws IOException {
        try {
            //回传参数  附加消息 参数必须进行UrlEncode之后才可以发送给支付宝
            String passbackParams = URLEncoder.encode("1", AliPayConfig.CHARSET);

            AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
            //该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。该参数数值不接受小数点， 如 1.5h，可转换为 90m。
            model.setTimeoutExpress("30m");
            //model.setSellerId("");
            model.setBody("移动端网页支付");
            model.setSubject(payModel.getProjectName());
            model.setTotalAmount(payModel.getAmount());
            model.setPassbackParams(passbackParams);
            model.setOutTradeNo(payModel.getTradeNo());
            model.setProductCode("QUICK_WAP_PAY");

            AliPayApi.wapPay(response, model, alipayClient, AliPayConfig.RETURN_URL, AliPayConfig.NOTIFY_URL);
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("创建订单失败，请稍后再试！");
        }
    }

    /**
     * APP支付
     */
    @ResponseBody
    @RequestMapping(value = "/appPay", method = RequestMethod.POST)
    public void appPay() throws IOException {

        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest alipayTradeAppPayRequest = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("APP端支付");
        model.setSubject(payModel.getProjectName());
        model.setOutTradeNo(payModel.getTradeNo());
        model.setTimeoutExpress("30m");
        model.setTotalAmount(payModel.getAmount());
        model.setProductCode("QUICK_MSECURITY_PAY");
        alipayTradeAppPayRequest.setBizModel(model);
        alipayTradeAppPayRequest.setNotifyUrl(AliPayConfig.NOTIFY_URL);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.sdkExecute(alipayTradeAppPayRequest);
            //就是orderString 可以直接给客户端请求，无需再做处理。
            System.out.println(alipayTradeAppPayResponse.getBody());
            //response.getWriter().write(URLEncoder.encode(alipayTradeAppPayResponse.getBody(), "UTF-8"));
            response.getWriter().write(alipayTradeAppPayResponse.getBody());

        } catch (AlipayApiException e) {
            e.printStackTrace();
            response.getWriter().write(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            response.getWriter().write(e.getMessage());
        }
    }

    /**
     * notify_url 异步通知  异步通知验签
     * 服务器后台通知,这个页面是程序后台运行的(买家和卖家都看不到)
     */
    @ResponseBody
    @RequestMapping("/notifyUrl")
    public void notifyUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String> params = AliPayApi.toMap(request);
        // data json格式
        String data = AliPayApi.getRequestData(request);
        //1.商户系统接收到异步通知以后，必须通过验签（验证通知中的sign参数）来确保支付通知是由支付宝发送的。详细验签规则参考异步通知验签
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.CHARSET, AliPayConfig.SIGNTYPE);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            response.getWriter().write("fail");
            return;
        }
        //2.接受到异步通知并验签通过后，一定要检查通知内容，包括通知中的app_id, out_trade_no, total_amount是否与请求中的一致，并根据trade_status进行后续业务处理。

        if (signVerified) {
            /* 实际验证过程建议商户务必添加以下校验：
                1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
                2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
                3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
                4、验证app_id是否为该商户（服务商）本身。
            */
            //通知返回的商户订单号
            String notifyOutTradeNo = params.get("out_trade_no");
            //通知返回的支付宝交易号
            String notifyTradeNo = params.get("trade_no");
            //通知返回的交易状态
            String notifyTradeStatus = params.get("trade_status");
            //通知返回的订单金额
            String notifyTotalAmount = params.get("total_amount");
            String notifyAppId = params.get("app_id");
            String notifySellerId = params.get("seller_id") == null ? "" : params.get("seller_id");
            String notifySellerEmail = params.get("seller_email") == null ? "" : params.get("seller_id");
            // TODO 后续操作
        } else {
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            response.getWriter().write("fail");
        }
    }


    /**
     * return_url 同步通知
     *
     * 买家付款成功后,如果接口中指定有return_url ,买家付完款后会跳到 return_url所在的页面,这个页面可以展示给客户看,这个页面只有付款成功才会跳转.
     */
    @ResponseBody
    @RequestMapping("/returnUrl")
    public ModelAndView returnUrl(){
        ModelAndView modelAndView = new ModelAndView();
        Map<String, String> params = AliPayApi.toMap(request);
        // data json格式
        String data = AliPayApi.getRequestData(request);
        //调用SDK验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, AliPayConfig.ALIPAY_PUBLIC_KEY, AliPayConfig.CHARSET, AliPayConfig.SIGNTYPE);
        } catch (AlipayApiException e) {
            //TODO 此处让用户自己判断有没有支付成功。
        }

        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            //通知返回的商户订单号
            String notifyOutTradeNo = params.get("out_trade_no");
            //通知返回的支付宝交易号
            String notifyTradeNo = params.get("trade_no");
            AlipayTradeQueryResponse tradeQueryResponse = queryTrade(notifyOutTradeNo, notifyTradeNo);

            if (tradeQueryResponse == null) {
                //TODO 提示系统繁忙（其实是查询支付宝订单报错）
            } else {
                if ("10000".equals(tradeQueryResponse.getCode())) {
                    if ("TRADE_SUCCESS".equals(tradeQueryResponse.getTradeStatus())) {
                        // TODO 交易成功
                    }
                    //TODO 其他状态判断
                } else {
                }
            }

        }else{
            //TODO 此处让用户自己判断有没有支付成功。
            // TODO 验签失败则记录异常日志，并在response中返回failure.
        }
        return modelAndView;
    }

    /**
     * 查询支付宝订单
     * @param outTradeNo
     * @param tradeNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryTrade", method = RequestMethod.GET)
    public AlipayTradeQueryResponse queryTrade(String outTradeNo, String tradeNo){
        AlipayTradeQueryRequest tradeQueryRequest = new AlipayTradeQueryRequest();
        tradeQueryRequest.setBizContent("{" +
                "\"out_trade_no\":" + outTradeNo + "," +
                "\"trade_no\":" + tradeNo +
                "}");
        AlipayTradeQueryResponse tradeQueryResponse = null;
        try {
            tradeQueryResponse = alipayClient.execute(tradeQueryRequest);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return null;
        }

        return tradeQueryResponse;
    }
}
