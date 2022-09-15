package com.qiyichain.msg.coin;

import java.math.BigDecimal;

public class BaseMsg {
    public static final BigDecimal GAS_LIMIT=new BigDecimal(5000000);
    public static final BigDecimal GAS_PRICE=new BigDecimal(1);

    private boolean success;
    private String hash;
    private String msg;

    private String data;

    public static BaseMsg buildError(String msg){
        BaseMsg dto=new BaseMsg();
        dto.setSuccess(false);
        dto.setMsg(msg);
        return dto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
