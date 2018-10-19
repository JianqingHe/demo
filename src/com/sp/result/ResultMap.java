package com.sp.result;

/**
 * 定义统一返回格式封装
 *
 * @author hejq
 * @date 2018-10-10 10:36
 */
public class ResultMap {

    /**
     * 返回状态码
     */
    private int code;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 信息提示
     */
    private String message;

    /**
     * 返回内容
     */
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultMap() {
    }

    public ResultMap(int status, String message) {
        if (ResultMapType.OK.code == status) {
            this.success = true;
        } else {
            this.success = false;
        }
        this.code = status;
        this.message = message;
    }

    public static ResultMap success(Object data) {
        ResultMap result = new ResultMap(ResultMapType.OK.code, ResultMapType.OK.phrase);
        result.data = data;
        return result;
    }

    /**
     * 操作成功返回结果
     *
     * @return ResultMap
     * @see ResultMap
     */
    public static ResultMap success() {
        return new ResultMap(ResultMapType.OK.code, ResultMapType.OK.phrase);
    }

    /**
     * 失败返回结果
     *
     * @param e 异常信息
     * @return ResultMap
     * @see ResultMap
     */
    public static ResultMap error(Exception e) {
        return new ResultMap(ResultMapType.ERROR.code, e.getMessage());
    }

    /**
     * 状态码
     */
    enum ResultMapType {

        /**
         * 成功
         */
        OK(1, "成功"),

        /**
         * 失败
         */
        ERROR(2, "失败");


        private int code;

        private String phrase;

        ResultMapType(int code, String phrase) {
            this.code = code;
            this.phrase = phrase;
        }

        public int getCode() {
            return code;
        }

        public ResultMapType setCode(int code) {
            this.code = code;
            return this;
        }

        public String getPhrase() {
            return phrase;
        }

        public ResultMapType setPhrase(String phrase) {
            this.phrase = phrase;
            return this;
        }
    }
}
