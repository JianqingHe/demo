package com.sp.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayTradeCreateModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.sp.config.AliPayConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * AliPayApi
 *
 * @author hejq
 * @date 2018-10-19 9:03
 */
public class AliPayApi {

    /**
     * 电脑网站支付(PC支付)
     * @param httpResponse
     * @param model
     * @param alipayClient
     * @param notifyUrl
     * @param returnUrl
     * @throws {AlipayApiException}
     * @throws IOException
     */
    public static void tradePage(HttpServletResponse httpResponse, AlipayTradePagePayModel model, AlipayClient alipayClient, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setBizModel(model);
        alipayRequest.setNotifyUrl(notifyUrl);
        alipayRequest.setReturnUrl(returnUrl);
        String form="";
        try {
            //调用SDK生成表单
            form = alipayClient.pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + AliPayConfig.CHARSET);
        //直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * WAP支付
     * @param response
     * @param model
     * @param returnUrl
     * @param notifyUrl
     * @return {String}
     * @throws {AlipayApiException}
     * @throws IOException
     */
    public static void wapPay(HttpServletResponse response, AlipayTradeWapPayModel model, AlipayClient alipayClient, String returnUrl, String notifyUrl) throws AlipayApiException, IOException {
        // 创建API对应的request
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        alipayRequest.setReturnUrl(returnUrl);
        // 在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(notifyUrl);
        // 填充业务参数
        alipayRequest.setBizModel(model);
        //调用SDK生成表单
        String form = alipayClient.pageExecute(alipayRequest).getBody();
        HttpServletResponse httpResponse = response;
        httpResponse.setContentType("text/html;charset=" + AliPayConfig.CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
    }

    /**
     * 统一收单交易创建接口
     * https://docs.open.alipay.com/api_1/alipay.trade.create
     * @param model
     * @param alipayClient
     * @param notifyUrl
     * @return {AlipayTradeCreateResponse}
     * @throws {AlipayApiException}
     */
    public static AlipayTradeCreateResponse tradeCreate(AlipayTradeCreateModel model, AlipayClient alipayClient, String notifyUrl) throws AlipayApiException{
        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        //model和biz_content同时存在的情况下取biz_content
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        return alipayClient.execute(request);
    }

    /**
     * 将异步通知的参数转化为Map
     * @param request
     * @return {Map<String, String>}
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>(1);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iterator = requestParams.keySet().iterator(); iterator.hasNext();) {
            String name = iterator.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用。
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 转为json格式数据
     *
     * @param request
     * @return
     */
    public static String getRequestData(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            params = toMap(request);
            return JSONUtils.toJSONString(params);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
