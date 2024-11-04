package com.claro.proceso.migracion.pospago.api.controllers;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;
import com.claro.proceso.migracion.pospago.api.services.mactpre.MigrationOrdersService;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/control-tracking")
public class MigrationOrderController {

    @Autowired
    private MigrationOrdersService service;

    @PatchMapping("/save-status")
    public ResponseEntity<ApiResponse> saveStatus(@Validated @RequestBody MigrationOrderEntity request) throws CRMPerException {
        return  ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.WS_REQUESTS, "guardar-estado",
                service.updateStepByStep(request)));
    }

    @GetMapping("/historico")
    public ResponseEntity<ApiResponse> getHisorico(){
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES,"hisorico",
                service.getAllData()));
    }
    @DeleteMapping("/delete/{msisdn}")
    public ResponseEntity<ApiResponse> deleteMsisdn(@PathVariable("msisdn") String msisdn) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.TRACKING,"borrado", service.deleteRegister(msisdn)));
    }

}
