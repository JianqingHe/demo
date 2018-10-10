package com.sp.config;

import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;

/**
 * 微信支付接口配置
 *
 * @author hejq
 * @date 2018-10-10 10:33
 */
public class WxConfig {

    /**
     * appid是微信公众账号或开放平台APP的唯一标识，在公众平台申请公众账号或者在开放平台申请APP账号后，
     * 微信会自动分配对应的appid，用于标识该应用。
     * 可在微信公众平台-->开发-->基本配置里面查看，商户的微信支付审核通过邮件中也会包含该字段值
     */
    public static String APPID = "";

    /**
     * 商户申请微信支付后，由微信支付分配的商户收款账号。
     */
    public static String MCHID = "";

    /**
     * 交易过程生成签名的密钥，仅保留在商户系统和微信支付后台，不会在网络中传播。
     * 商户妥善保管该Key，切勿在网络中传输，不能在其他客户端中存储，保证key不会被泄漏。
     * 商户可根据邮件提示登录微信商户平台进行设置。
     * 也可按一下路径设置：微信商户平台(pay.weixin.qq.com)-->账户中心-->账户设置-->API安全-->密钥设置
     */
    public static String MCHKEY = "";

    /**
     * AppSecret是APPID对应的接口密码，用于获取接口调用凭证access_token时使用。
     * 在微信支付中，先通过OAuth2.0接口获取用户openid，此openid用于微信内网页支付模式下单接口使用。
     * 可登录公众平台-->微信支付，获取AppSecret（需成为开发者且帐号没有异常状态）
     */
    public static String APPSECRET = "";

    public static String Token = "";

    public static String oauthUserScope = "";

    /**
     * app支付开放平台对应商户平台商户号
     */
    public static String OPENMCHID = "";

    /**
     * app支付开放平台对应商户平台商户密钥
     */
    public static String OPENMCHKEY = "";

    /**
     * app支付开放平台应用appID
     */
    public static String OPENAPPID = "";

    /**
     * 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
     */
    public static String NOTIFYURL = "";

    /**
     * 设置 wxPayUnifiedOrderRequest相关新
     * @return wxPayUnifiedOrderRequest
     */
    public WxPayUnifiedOrderRequest convert() {
        WxPayUnifiedOrderRequest wxPayUnifiedOrderRequest = new WxPayUnifiedOrderRequest();

        //否 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB
        wxPayUnifiedOrderRequest.setDeviceInfo("WEB");
        //是 随机字符串，不长于32位。
        wxPayUnifiedOrderRequest.setNonceStr(String.valueOf(System.currentTimeMillis()));

        /**
         * 商户申请微信支付后，由微信支付分配的商户收款账号。
         */
        wxPayUnifiedOrderRequest.setMchId(WxConfig.MCHID);
        //否 商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”

        /**
         *  否 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
         */
        wxPayUnifiedOrderRequest.setAttach("这是附加数据");
        //是 签名
        wxPayUnifiedOrderRequest.setNotifyURL(WxConfig.NOTIFYURL);
        //是 接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
        wxPayUnifiedOrderRequest.setNotifyURL(WxConfig.NOTIFYURL);
        wxPayUnifiedOrderRequest.setAppid(WxConfig.APPID);
        wxPayUnifiedOrderRequest.setBody("laohe-test");
        return wxPayUnifiedOrderRequest;
    }

}
