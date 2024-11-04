package com.claro.proceso.migracion.pospago.api.models;


import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;

public class CRMPerException extends Exception{
    private static final long serialVersionUID = 112341234234L;

    private ApiResponse apiResponse;

    public CRMPerException(ApiResponse apiResponse) {
        super(apiResponse.getErrorDescription());
        this.apiResponse = apiResponse;
    }

    public CRMPerException() {
        super();
    }

    public CRMPerException(String message) {
        super(message);
    }

    public CRMPerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CRMPerException(Throwable cause) {
        super(cause);
    }

    public CRMPerException(Throwable cause, ApiResponse apiResponse) {
        super(cause);
        this.apiResponse = apiResponse;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(ApiResponse apiResponse) {
        this.apiResponse = apiResponse;
    }

}
