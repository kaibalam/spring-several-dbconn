package com.claro.proceso.migracion.pospago.api.models.responses;

public class WebServiceException extends RuntimeException {
    public WebServiceException(){

    }
    public WebServiceException(String message){
        super(message);
    }
}
