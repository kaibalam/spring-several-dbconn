package com.claro.proceso.migracion.pospago.api.controllers;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;
import com.claro.proceso.migracion.pospago.api.services.bscs.BscsService;
import com.claro.proceso.migracion.pospago.api.services.mactpre.MigrationOrdersService;
import com.claro.proceso.migracion.pospago.api.services.migration.MigrationFlowService;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.claro.proceso.migracion.pospago.api.models.enums.StageProcess.FINISHED_FLOW;

@RestController
@RequestMapping("/migration-flow")
@CrossOrigin(origins = {"http://localhost:5173"})
@Slf4j
public class MigrationFlowController {

    @Autowired
    private MigrationOrdersService service;
    @Autowired
    private MigrationFlowService flow;
    @Autowired
    private BscsService bscs;

    @PostMapping("/saveOrder")
    public ResponseEntity<ApiResponse> saveOrder(@Validated @RequestBody MigrationOrderEntity entity) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.TRACKING, "history",
                service.saveRecord(entity)));
    }

    @PatchMapping("/update-parcial-order")
    public ResponseEntity<ApiResponse> updateParcialOrder(@Validated @RequestBody MigrationOrderEntity entity) throws CRMPerException{
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.TRACKING, "history-tracking",
                service.updateOrder(entity)));
    }

    @GetMapping("/find-order-by/{msisdn}")
    public ResponseEntity<ApiResponse> findOrderByMsisdn(@PathVariable("msisdn") String msisdn) throws CRMPerException{
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.TRACKING, "getting-order",
                service.isOrderPending(msisdn)));
    }

    @PostMapping("/migrar")
    public ResponseEntity<ApiResponse> migrateNumber(@Validated @RequestBody ResponseClientDataDTO request) throws CRMPerException{
        //Migrar
        Map<Integer, Map<String, Object>> response = flow.startMigrationFlow(request); //original
        log.info("respuesta de la migración {}",response);
        for (Map.Entry<Integer, Map<String, Object>> entry : response.entrySet()){
            log.info("info del entry {}",entry);
            if (entry.getValue().containsKey(FINISHED_FLOW.toString())){
                log.info("Si se completó actualizar el reason en contract history");
                //Guardar razón de migración en contract history
                bscs.setMigrationReason(request);
            }
        }
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.TRACKING, "migrationFlow",
                response));
    }

    @PostMapping("/test-drop-pospaid")
    public ResponseEntity<ApiResponse> testServer(@Validated @RequestBody ResponseClientDataDTO request) throws CRMPerException{
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.TRACKING, "test",
                flow.stepDropPost(request)));
    }

}
