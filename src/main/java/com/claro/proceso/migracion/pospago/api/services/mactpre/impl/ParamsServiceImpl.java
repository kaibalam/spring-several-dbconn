package com.claro.proceso.migracion.pospago.api.services.mactpre.impl;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.ParamsEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.responses.ParameterAlreadyExistsException;
import com.claro.proceso.migracion.pospago.api.models.responses.ResourceNotFoundException;
import com.claro.proceso.migracion.pospago.api.models.responses.ResourceNotValidException;
import com.claro.proceso.migracion.pospago.api.repositories.mactpre.ParamsRepository;
import com.claro.proceso.migracion.pospago.api.services.mactpre.ParamsService;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import com.claro.proceso.migracion.pospago.api.utils.ErrorHandleMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class ParamsServiceImpl implements ParamsService {
    @Autowired
    private ParamsRepository repo;

    @Override
    public Optional<ParamsEntity> findById(Long id) throws CRMPerException {
        ParamsEntity entity = repo.findById(id).orElseThrow(
                () -> new CRMPerException(ApiResponseUtils.success(ApiResponseType.PARAMS, ApiResponseUtils.ERR_204,
                        String.format("El id %s ingresado no existe en la tabla de parametros!", id)))
        );
        return Optional.ofNullable(entity);
    }

    @Override
    public List<ParamsEntity> findAll() {
        List<ParamsEntity> listaParams = repo.findAll();
        if (CollectionUtils.isEmpty(listaParams)) {
            return new ArrayList<>();
        }
        return repo.findAll();
    }

    @Override
    public Optional<ParamsEntity> findByDicTag(String tag) throws CRMPerException {
        ParamsEntity param = repo.findByDicTag(tag).orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorHandleMessages.ERROR_PARMAM_DOES_NOT_EXISTS, tag)));
        return Optional.of(param);
    }

    @Override
    public ParamsEntity createNewParam(ParamsEntity requestDTO) throws CRMPerException {
        boolean existBefore = repo.existsByDicTag(requestDTO.getDicTag());
        if (requestDTO.getId() == null && !existBefore) {
            return repo.save(requestDTO);
        } else {
            throw new ParameterAlreadyExistsException(String.format(ErrorHandleMessages.ERROR_PARMAM_ALREADY_EXISTS, requestDTO.getDicTag()));
        }
    }

    @Override
    public ParamsEntity updaterEntity(ParamsEntity entity) throws CRMPerException {
        boolean existsBefore = repo.existsById(entity.getId());
        if (existsBefore){
            return repo.save(entity);
        } else {
            throw new ResourceNotFoundException(String.format(ErrorHandleMessages.ERROR_PARMAM_DOES_NOT_EXISTS,entity.getDicTag()));
        }
    }

    @Override
    public ParamsEntity dropParam(Long id) {
        ParamsEntity entity = repo.findById(id).orElseThrow(() -> new ResourceNotValidException( String.format("No se encontro el id %s",id)));
        if (entity != null){
            repo.delete(entity);
        }
        return entity;
    }
}
