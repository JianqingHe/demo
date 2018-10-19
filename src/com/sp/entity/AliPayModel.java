package com.sp.entity;

/**
 * 支付宝参数（测试）
 *
 * @author hejq
 * @date 2018-10-19 9:15
 */
public class AliPayModel {

    /**
     * 交易标识
     */
    private String tradeNo = "laohe";

    /**
     * 交易额
     */
    private String amount = "0.01";

    /**
     * 项目名称
     */
    private String projectName = "test";

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
