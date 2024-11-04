package com.claro.proceso.migracion.pospago.api.repositories.mactpre;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrationOrderRepository extends JpaRepository<MigrationOrderEntity, Long>, JpaSpecificationExecutor<MigrationOrderEntity> {

    Boolean existsByOrderId(Integer id) throws CRMPerException;
    MigrationOrderEntity findByMsisdn(String msisdn) throws CRMPerException;
    MigrationOrderEntity findByOrderId(Integer orderId) throws CRMPerException;
}
