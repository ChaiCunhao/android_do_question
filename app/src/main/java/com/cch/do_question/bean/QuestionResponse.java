package com.cch.do_question.bean;

import java.util.HashMap;
import java.util.Map;

public class QuestionResponse {
    private int code;
    private Map<String,String> msg;
    private Map<String,String> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getMsg() {
        return msg;
    }

    public void setMsg(Map<String, String> msg) {
        this.msg = msg;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
