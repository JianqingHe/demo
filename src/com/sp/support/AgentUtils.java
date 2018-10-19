package com.sp.support;

import javax.servlet.http.HttpServletRequest;

/**
 * IP工具
 *
 * @author hejq
 * @date 2018-10-19 9:50
 */
public class AgentUtils {

    public AgentUtils() {
    }

    public static String getIp(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        if(ipAddress != null && ipAddress.length() > 15 && ipAddress.indexOf(",") > 0) {
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
        }

        if(ipAddress != null && "0:0:0:0:0:0:0:1".equals(ipAddress)) {
            ipAddress = "127.0.0.1";
        }

        return ipAddress;
    }
}
