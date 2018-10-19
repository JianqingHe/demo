package com.sp.config;

/**
 * 支付宝config
 *
 * @author hejq
 * @date 2018-10-19 9:17
 */
public class AliPayConfig {

	/**
	 * 合作身份者ID，以2088开头由16位纯数字组成的字符串
	 */
	public static String partner = "";

    /**
     * 收款支付宝账号，一般情况下收款账号就是签约账号
     */
	public static String SELLER_ID = "";

    /**
     * 卖家邮箱
     */
	public static String SELLER_EMAIL = "";

    /**
     * 商户的私钥
     */
	public static String key = "";

    /**
     * 收款方账号沙箱
     *
     */
	public static String SB_DO_MAIN = "";

    /**
     * 商户appid   沙箱   正式
     */
	public static String SB_APPID = "";

    /**
     * 私钥 pkcs8格式的
     */
	public static String SB_RSA_PRIVATE_KEY = "";

    /**
     * 服务器异步通知页面路径
     * 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     */
	public static String SB_NOTIFY_URL = SB_DO_MAIN + "/alipay/notifyUrl";

    /**
     * 	页面跳转同步通知页面路径
     * 	需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
     * 	商户可以自定义同步跳转地址
     */
	public static String SB_RETURN_URL = SB_DO_MAIN + "/alipay/returnUrl";

    /**
     * 授权回调地址
     */
	public static String SB_REDIRECT_URL = SB_DO_MAIN + "/alipay/redirectUrl";

    /**
     * 支付宝公钥
      */
	public static String SB_ALIPAY_PUBLIC_KEY = "";

    /**
     * 请求网关地址
     * 沙箱 https://openapi.alipaydev.com/gateway.do
     */
	public static String SB_GATEWAY = "https://openapi.alipaydev.com/gateway.do";

    /**
     * 账号
     */
	public static String DO_MAIN = "";

    /**
     * appId
     */
	public static String APPID = "";

    /**
     * 私钥
     */
	public static String RSA_PRIVATE_KEY = "";

    /**
     * 通知URL
     */
	public static String NOTIFY_URL = DO_MAIN + "/alipay/notifyUrl";

    /**
     * 返回URL
     */
	public static String RETURN_URL = DO_MAIN + "/alipay/returnUrl";

    /**
     * 重定向
     */
	public static String REDIRECT_URL = DO_MAIN + "/alipay/redirectUrl";

    /**
     * 公钥
     */
	public static String ALIPAY_PUBLIC_KEY = "";

    /**
     * gateWay
     */
	public static String GATEWAY = "https://openapi.alipay.com/gateway.do";

    /**
     * 编码
     */
	public static String CHARSET = "UTF-8";

    /**
     * 签名方式 不需修改
     */
	public static String sign_type = "MD5";

    /**
     * 返回格式
     */
	public static String FORMAT = "json";

    /**
     * 日志记录目录
     */
	public static String log_path = "/log";

    /**
     * 签名 RSA2
     */
	public static String SIGNTYPE = "RSA2";

}
