package com.claro.proceso.migracion.pospago.api.services.mactpre.impl;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.responses.OrderAlreadyExistsException;
import com.claro.proceso.migracion.pospago.api.models.responses.ResourceNotFoundException;
import com.claro.proceso.migracion.pospago.api.repositories.mactpre.MigrationOrderRepository;
import com.claro.proceso.migracion.pospago.api.services.mactpre.MigrationOrdersService;
import com.claro.proceso.migracion.pospago.api.utils.ErrorHandleMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.claro.proceso.migracion.pospago.api.utils.MapperUtils.buildMigrationEntity;
import static com.claro.proceso.migracion.pospago.api.utils.MapperUtils.updateFieldIfChanged;

@Service
@Slf4j
public class MigrationOrdersServiceImpl implements MigrationOrdersService {

    @Autowired
    private MigrationOrderRepository repo;

    @Override
    public MigrationOrderEntity saveRecord(MigrationOrderEntity entity) throws CRMPerException {
        boolean existsEntity = repo.existsByOrderId(entity.getOrderId());
        if (existsEntity) {
            throw new OrderAlreadyExistsException(String.format(ErrorHandleMessages.ERROR_ORDER_ALREADY_EXISTS, entity.getOrderId()));
        }
        return repo.save(entity);
    }

    @Override
    public MigrationOrderEntity updateOrder(MigrationOrderEntity entity) throws CRMPerException {
        boolean isExistsOrder = repo.existsByOrderId(entity.getOrderId());
        if (isExistsOrder) {
            return repo.save(entity);
        } else {
            throw new OrderAlreadyExistsException(String.format(ErrorHandleMessages.ERROR_ORDER_DOES_NOT_EXISTS, entity.getOrderId()));
        }
    }

    @Override
    public MigrationOrderEntity isOrderPending(String msisdn) throws CRMPerException {
        log.info("Buscar si hay orden para este número");
        MigrationOrderEntity existentOrder = repo.findByMsisdn(msisdn);
        if (existentOrder != null) {
            log.info("El número ya posee una orden anterior de migración");
            return existentOrder;
        }
        log.info("El número no tiene ninguna orden de migración anterior");
        return new MigrationOrderEntity();
    }

    @Override
    public MigrationOrderEntity updateStepByStep(MigrationOrderEntity req) throws CRMPerException {
        log.info(String.format("Guardar traking para la orden %s con estado %s",req.getOrderId(),req.getOrderStatus()));
        MigrationOrderEntity migrationOrder = repo.findByOrderId(req.getOrderId());
        if (migrationOrder == null){
            throw new ResourceNotFoundException(String.format(" No hay registro de la orden %s solicitada!",req.getOrderId()));
        }

        MigrationOrderEntity newValues = buildMigrationEntity(req);
        newValues.setMigrationEndDate(LocalDateTime.now());

        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getRequestPayload,migrationOrder::setRequestPayload);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getResponsePayload,migrationOrder::setResponsePayload);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getStatusMigration,migrationOrder::setStatusMigration);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getOrderStatus,migrationOrder::setOrderStatus);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getMigrationEndDate,migrationOrder::setMigrationEndDate);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getStageProcess,migrationOrder::setStageProcess);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getOdaCode,migrationOrder::setOdaCode);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getUserId,migrationOrder::setUserId);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getArea,migrationOrder::setArea);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getRegion,migrationOrder::setRegion);
        updateFieldIfChanged(migrationOrder,newValues, MigrationOrderEntity::getAgency,migrationOrder::setAgency);

        log.info("Guardado el estado de la orden");
        return repo.save(migrationOrder);
    }

    @Override
    public List<MigrationOrderEntity> getAllData() {
        return repo.findAll();
    }

    @Override
    public MigrationOrderEntity deleteRegister(String msisdn) throws CRMPerException {
        MigrationOrderEntity entity = isOrderPending(msisdn);
        repo.delete(entity);
        log.info("Registro de orden eliminada!");
        return entity;
    }
}
