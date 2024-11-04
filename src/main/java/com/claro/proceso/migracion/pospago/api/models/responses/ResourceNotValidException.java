package com.claro.proceso.migracion.pospago.api.models.responses;

public class ResourceNotValidException extends RuntimeException{
    public ResourceNotValidException(){

    }
    public ResourceNotValidException(String message){
        super(message);
    }
}
