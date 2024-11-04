package com.claro.proceso.migracion.pospago.api.models.responses;

public class ErrorWebServiceConnectionRefusedException extends RuntimeException {

    public ErrorWebServiceConnectionRefusedException(){

    }
    public ErrorWebServiceConnectionRefusedException(String message){
        super(message);
    }
}
