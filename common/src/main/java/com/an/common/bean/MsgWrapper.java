package com.an.common.bean;

public class MsgWrapper<T> {

    T wrapper;
    String code;
    String message;

    public MsgWrapper() {
    }

    public MsgWrapper(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public MsgWrapper(T wrapper, String code, String message) {
        this.wrapper = wrapper;
        this.code = code;
        this.message = message;
    }

    public T getWrapper() {
        return wrapper;
    }

    public void setWrapper(T wrapper) {
        this.wrapper = wrapper;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
