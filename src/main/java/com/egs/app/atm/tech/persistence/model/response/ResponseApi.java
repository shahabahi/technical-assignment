package com.egs.app.atm.tech.persistence.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseApi<T> {


    public ResponseApi(byte errorCode, T data) {
        this.data = data;
        this.errorCode = errorCode;
    }

    public ResponseApi(byte errorCode, String message ) {
        this.message = message;
        this.errorCode = errorCode;
    }


    @JsonProperty("data")
    private T data;



    @JsonProperty("message")
    private String message;

    @JsonProperty("error_code")
    private byte errorCode;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }
}
