package com.claro.proceso.migracion.pospago.api.models.responses;

public class EncodingNotSupportedExcpetion extends RuntimeException{
    public EncodingNotSupportedExcpetion(){

    }
    public EncodingNotSupportedExcpetion(String message){
        super(message);
    }
}
