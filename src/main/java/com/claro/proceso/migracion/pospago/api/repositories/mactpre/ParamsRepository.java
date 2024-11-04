package com.claro.proceso.migracion.pospago.api.repositories.mactpre;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.ParamsEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParamsRepository extends JpaRepository<ParamsEntity, Long>, JpaSpecificationExecutor<ParamsEntity> {

    Optional<ParamsEntity> findByDicTag(String tag) throws CRMPerException;
    Boolean existsByDicTag(String tag) throws CRMPerException;

}
