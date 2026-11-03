package com.taskflow.vegiees.dto;

import java.util.Map;

public class ErrorResponse {

    private String message;
    private Map<String , String> errors;

    public ErrorResponse(){

    }

    public ErrorResponse(String message) {
        this.message = message;
        this.errors = null;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }


}
