package com.example.api.response;

import java.util.Map;

public class AddingPostResponse {

    private boolean result;
    private Map<String, String> errors;

    public AddingPostResponse() {
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
