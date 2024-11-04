package com.claro.proceso.migracion.pospago.api.services.migration;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.enums.StageProcess;
import com.claro.proceso.migracion.pospago.api.models.enums.StageStatus;

import java.util.Map;

public interface MigrationFlowService {
    //flujo
    String  selectMigrationType(String msisdn) throws CRMPerException;
    Map<Integer, Map<String,Object>> startMigrationFlow(ResponseClientDataDTO request) throws CRMPerException;
    Map<Integer, Map<String, Object>> startWholeFlow(ResponseClientDataDTO request, String typeService) throws  CRMPerException;
    Map<Integer, Map<String, Object>> retryMigrationFlow(ResponseClientDataDTO request, Integer orderId, StageProcess process, StageStatus status) throws  CRMPerException;
    String stepDropPost(ResponseClientDataDTO req) throws CRMPerException;
    String stepDropValidation(ResponseClientDataDTO req) throws CRMPerException;
    String stepActivatePrepaid(ResponseClientDataDTO req) throws CRMPerException;
    String stepDropPrepaidSoapC(ResponseClientDataDTO req) throws CRMPerException;
    String stepDropPrepaidWS(ResponseClientDataDTO req) throws CRMPerException;
    String stepSendHeaderODA(ResponseClientDataDTO req) throws CRMPerException;
    String stepSendDetailODA(ResponseClientDataDTO req) throws CRMPerException;
}
