package com.claro.proceso.migracion.pospago.api.controllers;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;
import com.claro.proceso.migracion.pospago.api.services.crmoe.CrmOeService;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crm-v9")
public class CrmV9Controller {

    @Autowired
    private CrmOeService service;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse> getAgencyData(@PathVariable(name = "userId") String userId) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES,"agencia-usuario",
                service.getAgencyObject(userId)));
    }

}
