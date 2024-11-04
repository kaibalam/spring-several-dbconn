package com.claro.proceso.migracion.pospago.api.models.responses;

public class ParameterAlreadyExistsException extends RuntimeException{

    public ParameterAlreadyExistsException(){

    }
    public ParameterAlreadyExistsException(String message){
        super(message);
    }
}
