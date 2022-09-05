package com.qiyichain.msg.coin;

import java.math.BigDecimal;

public class FastMsg {
    private boolean success;
    private String hash;
    private String msg;

    private String data;

    public static FastMsg buildError(String msg){
        FastMsg dto=new FastMsg();
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
