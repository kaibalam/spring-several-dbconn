package com.claro.proceso.migracion.pospago.api.models.responses;

public class FlowMigrationException extends RuntimeException {
    public FlowMigrationException(){

    }
    public FlowMigrationException(String message){
        super(message);
    }
}
