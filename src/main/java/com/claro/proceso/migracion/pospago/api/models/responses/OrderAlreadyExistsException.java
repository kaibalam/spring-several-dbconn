package com.claro.proceso.migracion.pospago.api.models.responses;

public class OrderAlreadyExistsException extends RuntimeException{
    public OrderAlreadyExistsException(){

    }
    public OrderAlreadyExistsException(String message){
        super(message);
    }
}
