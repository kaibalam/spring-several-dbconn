package com.claro.proceso.migracion.pospago.api.controllers;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.ParamsEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;
import com.claro.proceso.migracion.pospago.api.services.mactpre.ParamsService;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/params")
public class ParamsController {

    @Autowired
    private ParamsService param;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listAll() throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.PARAMS, "params",
                param.findAll()));
    }

    @GetMapping("/param/{tag}")
    public ResponseEntity<ApiResponse> findTag(@PathVariable("tag") String tag) throws CRMPerException {
        Optional<ParamsEntity> par = param.findByDicTag(tag);
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.PARAMS, "param",
                par.get().getId() == null?String.format("El parametro \'%s\' solicitado no existe en la tabla de parametros",tag):par));
    }

    @PostMapping("/create-tag")
    public ResponseEntity<ApiResponse> createNewParam(@Validated @RequestBody ParamsEntity newParam) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.PARAMS, "create-param", param.createNewParam(newParam)));
    }

    @PutMapping("/update-tag")
    public ResponseEntity<ApiResponse> updateParam(@Validated @RequestBody ParamsEntity entity) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.PARAMS, "update-param",
                param.updaterEntity(entity)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteParam(@PathVariable("id") Long id) throws CRMPerException{
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES,"deleted",
                param.dropParam(id)));
    }


}
