package com.example.api.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterResponse {

    private boolean result;

    private Map<String, String> errors;

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
