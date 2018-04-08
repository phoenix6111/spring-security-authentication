package com.phoenix.security.core.support;

public class SimpleResponse {

    private String content;

    public SimpleResponse(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
