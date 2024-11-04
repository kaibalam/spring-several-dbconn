package com.claro.proceso.migracion.pospago.api.services.mactpre;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;

import java.util.List;

public interface MigrationOrdersService {

    MigrationOrderEntity saveRecord(MigrationOrderEntity entity) throws CRMPerException;
    MigrationOrderEntity updateOrder(MigrationOrderEntity entity) throws CRMPerException;
    MigrationOrderEntity isOrderPending(String msisdn) throws CRMPerException;
    MigrationOrderEntity updateStepByStep(MigrationOrderEntity request) throws CRMPerException;
    List<MigrationOrderEntity> getAllData();
    MigrationOrderEntity deleteRegister(String msisdn) throws CRMPerException;
}
