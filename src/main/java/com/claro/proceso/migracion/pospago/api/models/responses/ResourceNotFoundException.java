package com.claro.proceso.migracion.pospago.api.models.responses;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(){

    }
    public ResourceNotFoundException(String message){
        super(message);
    }
}
