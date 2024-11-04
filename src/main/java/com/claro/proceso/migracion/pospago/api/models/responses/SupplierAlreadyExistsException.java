package com.claro.proceso.migracion.pospago.api.models.responses;

public class SupplierAlreadyExistsException extends RuntimeException{

    public SupplierAlreadyExistsException(){}
    public SupplierAlreadyExistsException(String message){
        super(message);
    }
}
