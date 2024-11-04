package com.claro.proceso.migracion.pospago.api.services.mactpre;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.ParamsEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;

import java.util.List;
import java.util.Optional;

public interface ParamsService {

    Optional<ParamsEntity> findById(Long id) throws CRMPerException;
    List<ParamsEntity> findAll() throws CRMPerException;
    Optional<ParamsEntity> findByDicTag(String tag) throws CRMPerException;
    ParamsEntity createNewParam(ParamsEntity entity) throws CRMPerException;

    ParamsEntity updaterEntity(ParamsEntity entity) throws CRMPerException;

    ParamsEntity dropParam(Long id);
}
