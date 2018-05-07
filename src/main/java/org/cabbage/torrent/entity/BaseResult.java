package org.cabbage.torrent.entity;

public class BaseResult {

    private Object data;

    private String msg;

    private int status = 1;

    public BaseResult(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public BaseResult(Object data) {
        this.data = data;
    }

    public BaseResult(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
