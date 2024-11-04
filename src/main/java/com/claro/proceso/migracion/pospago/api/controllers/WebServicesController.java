package com.claro.proceso.migracion.pospago.api.controllers;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.MasterCodeRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.PrepaidServicesRequest;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;
import com.claro.proceso.migracion.pospago.api.models.responses.WebServiceException;
import com.claro.proceso.migracion.pospago.api.services.migration.MigrationFlowService;
import com.claro.proceso.migracion.pospago.api.services.wsrequests.impl.WebRequestsServicesImpl;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.claro.proceso.migracion.pospago.api.utils.MapperUtils.buildRequestActivePrep;
import static com.claro.proceso.migracion.pospago.api.utils.MapperUtils.buildRequestODADetail;


@RestController
@RequestMapping("/ws-request-flow")
@CrossOrigin(origins = {"http://localhost:5173"})
@Slf4j
public class WebServicesController {

    @Autowired
    private WebRequestsServicesImpl services;
    @Autowired
    private MigrationFlowService mig;

    @PostMapping("/master-code")
    public ResponseEntity<ApiResponse> validateMasterCode(@Validated @RequestBody MasterCodeRequest request) throws CRMPerException {
        String masterCodeResponse =  services.validateMasterCode(request);
        log.info("Response del WS código maestro {}", masterCodeResponse);
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.WS_REQUESTS, "masterCode", masterCodeResponse));
    }

    @GetMapping("/verify-type/{msisdn}")
    public ResponseEntity<ApiResponse> verifyPostpaidType(@PathVariable("msisdn") String msisdn) throws CRMPerException {
        String cleanSPR = services.cleanCacheSPR(msisdn);
        if (!cleanSPR.contains("\"code\":0,\"description\":\"Successful Transaction\"")) {
            throw new WebServiceException(String.format("El servicio para limpiar cache no esta funcionando %s", cleanSPR));
        }
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.WS_REQUESTS, "tipoPosgao",
                mig.selectMigrationType(msisdn)));
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleSubscriber(@Validated @RequestBody DropPrepaidRequest request) throws CRMPerException {
        String respuesta = services.dropPrepaid(request);
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.WS_REQUESTS, "borrado",
                services.dropPrepaid(request)));
    }

    @PostMapping("/activatePrepaid")
    public ResponseEntity<ApiResponse> activatePrepaid(@Validated @RequestBody ResponseClientDataDTO request) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.WS_REQUESTS, "activado",
                services.activatePrepaid(buildRequestActivePrep(request,new PrepaidServicesRequest(0,0,0)),"HIBRIDO")));
    }

    @PostMapping("/odaHeader")
    public ResponseEntity<ApiResponse> sendOdaDetail(@Validated @RequestBody ResponseClientDataDTO request) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.WS_REQUESTS, "odaHeader",
                services.detailODARecharge(buildRequestODADetail(request))));
    }
}
