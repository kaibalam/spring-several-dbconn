package com.claro.proceso.migracion.pospago.api.services.migration.impl;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.enums.OrderStatus;
import com.claro.proceso.migracion.pospago.api.models.enums.StageProcess;
import com.claro.proceso.migracion.pospago.api.models.enums.StageStatus;
import com.claro.proceso.migracion.pospago.api.models.requests.PrepaidServicesRequest;
import com.claro.proceso.migracion.pospago.api.models.responses.FlowMigrationException;
import com.claro.proceso.migracion.pospago.api.models.responses.FlowMigrationStageResponse;
import com.claro.proceso.migracion.pospago.api.services.mactpre.MigrationOrdersService;
import com.claro.proceso.migracion.pospago.api.services.migration.MigrationFlowService;
import com.claro.proceso.migracion.pospago.api.services.wsrequests.WebRequestService;
import com.claro.proceso.migracion.pospago.api.wsdl.DeleteResponse;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.claro.proceso.migracion.pospago.api.utils.MapperUtils.*;


@Service
@Slf4j
public class MigrationFlowServiceImpl implements MigrationFlowService {
    @Autowired
    private WebRequestService reqWS;
    @Autowired
    private MigrationOrdersService migService;


    private String dropResponse = "{\"hasError\":true,\"code\":562,\"message\":\"success\",\"attributes\":[{\"name\":\"downPostpaid\",\"value\" }]}{\"hasError\":true,\"code\":0,\"message\":\"Failed\",\"faultDetails\":{\"errorCode\":500,\"errorMessage\":\"Type definition error: [simple type, class org.json.JSONObject]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.json.JSONObject and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.claro.bscs8.dto.ExitStatusDTO[\"attributes\"]->java.util.ArrayList[0]->com.claro.bscs8.dto.AttributesDTO[\"value\"])\",\"errorDetails\":\"org.springframework.http.converter.HttpMessageConversionException: Type definition error: [simple type, class org.json.JSONObject]; nested exception is com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.json.JSONObject and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: com.claro.bscs8.dto.ExitStatusDTO[\"attributes\"]->java.util.ArrayList[0]->com.claro.bscs8.dto.AttributesDTO[\"value\"])\"}}";
    private String activatePrepaid = "{" +
            "\"hasError\": false," +
            "\"code\": 4730," +
            "\"message\": \"Contrato Prepago creado con éxito\"," +
            "\"attributes\": [" +
            "{" +
            "\"name\": \"prepaidContractResult\"," +
            "\"value\": {" +
            "\"status\": null," +
            "\"exitMessage\": null," +
            "\"errorMessage\": null," +
            "\"operationCode\": null," +
            "\"idlColasIdlRequest\": null," +
            "\"customerId\": 55454," +
            "\"contractId\": 546456," +
            "\"imsi\": null," +
            "\"prepaidServicesRes\": null," +
            "\"phoneNumber\": \"50212009379\"," +
            "\"exitCode\": null," +
            "\"colasIdlRequest\": null" +
            "}" +
            "}" +
            "]" +
            "}";
    private String postType;
    private Map<Integer, Map<String, Object>> responseStagedDTO = new HashMap<>();
    private Map<String, Object> content;
    private int count = 0;
    private String headerODAResponse = "{" +
            "\"hasError\": false," +
            "\"code\": 1689655442," +
            "\"message\": \"Encabezado  enviando a ODA con exito\"," +
            "\"faultDetails\": null," +
            "\"attributes\": null" +
            "}";
    String odaCode = null;

    @Override
    public String selectMigrationType(String msisdn) throws CRMPerException {
        log.info("Se valida el tipo de linea");
        try {
            String cleaned = reqWS.cleanCacheSPR(msisdn);
            log.info("Se limpió el cache en SPR {}", cleaned);
        } catch (Exception e) {
            log.error("Falló en el consumo del cache del SPR");
        }
        String paymentType = methodType(msisdn);
        log.info("El tipo de linea es: {}", paymentType);
        if (paymentType != null) return paymentType;
        return "No se obtuvo respuesta al consultar tipo de linea!";
    }

    private String methodType(String msisdn) throws CRMPerException {
        String responseType = reqWS.queryPostpaidTypeSPR(msisdn);
        log.info("Response de consulta SPR {}", responseType);
        if (responseType.contains("paymentMethodInd")) {
            try {
                JSONObject json = new JSONObject(responseType);
                String tipo = json.getString("paymentMethodInd");
                switch (tipo) {
                    case "A":
                        return "PREPAGO";
                    case "P":
                        return "POSPAGO";
                    case "H":
                        return "HIBRIDO";
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public Map<Integer, Map<String, Object>> startMigrationFlow(ResponseClientDataDTO request) throws CRMPerException {
        log.info("iniciando flujo de migración");
        //TODO iniciarflow descomentanto este servicio
        //postType = "HIBRIDO";//desarrollo
        postType = selectMigrationType(request.getData().getMsisdn()); //produccion
        log.info("Response de consulta SPR {}", postType);
        MigrationOrderEntity stageStatus = migService.isOrderPending(request.getData().getMsisdn());
        content = new HashMap<>();
        responseStagedDTO = new HashMap<>();
        count = 0;
        content.put(String.format("Inicios de flujo de migración pospago %s", postType), stageStatus.getId() != null ? String.format("se encontró una orden %s para este msisdn %s", stageStatus.getOrderId(), request.getData().getMsisdn()) : "No hay ordenes previas para este número");
        responseStagedDTO.put(count++, content);
        if (stageStatus.getOrderId() == null) {
            startWholeFlow(request, postType);
        } else {
            if (stageStatus.getOrderStatus().equals(OrderStatus.COMPLETED)) {
                //TODO Agregar un flujo para reenviar la orde media vez ya exista y este compleated y exitoso
                if (stageStatus.getStatusMigration().equals(StageStatus.SUCCESS) && stageStatus.getStageProcess().equals(StageProcess.FINISHED_FLOW)
                        && isDayAfter(stageStatus.getMigrationEndDate(), LocalDateTime.now())) {
                    startWholeFlow(request, postType);
                } else {
                    content.put("odacode", stageStatus.getOdaCode());
                    content.put("completed", String.format("la orden %s fue efectuada %s y se encuentra en estado %s", stageStatus.getOrderId(), stageStatus.getMigrationEndDate(), stageStatus.getOrderStatus()));
                }
            } else {
                request.getData().setOrderNumber(stageStatus.getOrderId());
                request.getAssessorParams().setOrderId(stageStatus.getOrderId());
                retryMigrationFlow(request, stageStatus.getOrderId(), stageStatus.getStageProcess(), stageStatus.getStatusMigration());
            }
        }
        return responseStagedDTO;
    }

    @Override
    public Map<Integer, Map<String, Object>> startWholeFlow(ResponseClientDataDTO request, String typeService) throws CRMPerException {
        log.info("inicio de flujo pospago completo");
        FlowMigrationStageResponse resp = new FlowMigrationStageResponse();
        MigrationOrderEntity entity = migService.saveRecord(
                new MigrationOrderEntity(
                        String.format("Orden %s a migrar el msisdn %s por el usuario %s como pospago %s",
                                request.getData().getOrderNumber(),
                                request.getData().getMsisdn(),
                                request.getAssessorParams().getUserId(),
                                typeService.equals("HIBRIDO") ? typeService : "PURO"),
                        request.getData().getMsisdn(), "GUA", LocalDateTime.now(), request.getAssessorParams().getUserId(),
                        request.getData().getOrderNumber(), request.getAssessorParams().getAmountProration(), request.getAssessorParams().getMasterCode(),
                        OrderStatus.IN_PROGRESS, StageProcess.DROP_POSTPAID, StageStatus.FAILED, request.getData().getAgencyObjectDTO().getArea(),
                        request.getData().getAgencyObjectDTO().getRegion(),request.getData().getAgencyObjectDTO().getAgencia()));
        stepDropPost(request);
        return responseStagedDTO;
    }


    @Override
    public Map<Integer, Map<String, Object>> retryMigrationFlow(ResponseClientDataDTO request, Integer orderId,
                                                                StageProcess process, StageStatus status) throws CRMPerException {
        log.info(String.format("Efectuando reintento desde %s", process));
        switch (process.toString()) {
            case "DROP_POSTPAID":
                if (status.equals(StageStatus.SUCCESS)) {
                    stepDropValidation(request);
                } else {
                    stepDropPost(request);
                }
                break;
            case "VALID_DROP_POST":
                if (status.equals(StageStatus.SUCCESS)) {
                    if (postType.equals("HIBRIDO")) {
                        //stepDropPrepaidWS(request); // wsimport
                        stepDropPrepaidSoapC(request); // soap client
                    } else {
                        stepActivatePrepaid(request);
                    }
                } else {
                    stepDropValidation(request);
                }
                break;
            case "DROP_PREPAID":
                if (status.equals(StageStatus.SUCCESS)) {
                    stepActivatePrepaid(request);
                } else {
                    //stepDropPrepaidWS(request); //wsimport
                    stepDropPrepaidSoapC(request); //soap client
                }
                break;
            case "ACTIVATE_PREPAID":
                if (status.equals(StageStatus.SUCCESS)) {
                    stepSendDetailODA(request);
                } else {
                    stepActivatePrepaid(request);
                }
                break;
            case "HEADER_ODA":
                if (status.equals(StageStatus.SUCCESS)) {
                    stepSendDetailODA(request);
                } else {
                    stepSendHeaderODA(request);
                }
                break;
            case "DETAIL_ODA":
                if (status.equals(StageStatus.SUCCESS)) {
                    return responseStagedDTO;
                } else {
                    stepSendHeaderODA(request);
                }
                break;
            default:


        }
        return responseStagedDTO;
    }

    @Override
    public String stepDropPost(ResponseClientDataDTO req) throws CRMPerException {
        log.info("Iniciando baja en pospago...");

        //String respDrop = dropResponse;// desarrollo
        String respDrop = reqWS.dropPostpaid(buildDropPost(req)); //produccion
        log.info("Response de dropPostpaid {}", respDrop);
        if (respDrop.contains("\"hasError\":false,\"code\":562,\"message\":\"success\"")) {
            respDrop = respDrop.replace("{\"name\":\"downPostpaid\",\"value\" }", "{\"name\":\"downPostpaid\",\"value\" : null }");
        }
        StageStatus response = StageStatus.FAILED;
        if (respDrop.contains("\"hasError\":false") && respDrop.contains("\"message\":\"success\",")) {
            log.info("Baja en pospago completada!");
            response = StageStatus.SUCCESS;
        }
        if (respDrop.contains("ERROR_CODE: RC6005-001")) {
            response = StageStatus.SUCCESS;
        }
        migService.updateStepByStep(new MigrationOrderEntity(buildDropPost(req).toString(), respDrop,
                req.getAssessorParams().getOrderId(), response, StageProcess.DROP_POSTPAID, OrderStatus.IN_PROGRESS));
        log.info("ir al siguiente paso validar la baja");
        responseStagedDTO.put(count++,
                saveFlowReturnObject(StageProcess.DROP_POSTPAID.toString(),
                        "Baja en pospago",
                        response.equals(StageStatus.SUCCESS) ? "Baja en pospago exitosa!" : "Error de baja en pospago")); //actual funcional

        if (response.equals(StageStatus.SUCCESS)) {
            stepDropValidation(req);
        } else {
            throw new FlowMigrationException("Error al dar baja en pospago");
        }
        return response.toString();
    }

    @Override
    public String stepDropValidation(ResponseClientDataDTO req) throws CRMPerException {
        log.info("Verificando estatus de la baja...");
        //String dropValidation = dropResponse;//desarrollo
        String dropValidation = reqWS.dropVerification(buildDropValidation(req)); //produccion
        log.info("Response de validación de baja {}", dropValidation);
        StageStatus response = StageStatus.FAILED;
        if (dropValidation.contains("\"hasError\":false")) {
            log.info("Verificada la baja exitosa!");
            Map<String, Object> json = jsonMapper(dropValidation, null);
            if (json.get("hasError").equals("false")) {
                response = StageStatus.SUCCESS;
            }
        }
        migService.updateStepByStep(new MigrationOrderEntity(buildDropValidation(req).toString(), dropValidation,
                req.getAssessorParams().getOrderId(), response, StageProcess.VALID_DROP_POST, OrderStatus.IN_PROGRESS));
        log.info("ir al siguiente paso si es híbrido baja prepago y si es puro alta a prepago");
        responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.VALID_DROP_POST.toString(), "Verificación de baja en Pospago", response.equals(StageStatus.SUCCESS) ? "Verificación de baja en pospago exitosa!" : "Error de baja en pospago"));
        if (response.equals(StageStatus.SUCCESS)) {
            if (postType.equals("HIBRIDO")) {
                //stepDropPrepaidWS(req);
                stepDropPrepaidSoapC(req);
            } else {
                stepActivatePrepaid(req);
            }
        } else {
            throw new FlowMigrationException("Error al validar la baja en pospago");
        }
        return response.toString();
    }

    @Override
    public String stepActivatePrepaid(ResponseClientDataDTO req) throws CRMPerException {
        log.info("iniciando activación Prepago...");
        //String prepaidActive = activatePrepaid;//desarrollo
        String prepaidActive = reqWS.activatePrepaid(
                buildRequestActivePrep(req,
                        new PrepaidServicesRequest(0, 0, 0)), postType); // producción
        log.info("Response de activación prepago {}", prepaidActive);
        StageStatus response = StageStatus.FAILED;
        Map<String, Object> json = jsonMapper(prepaidActive, null);
        if (json.get("hasError").equals("false")) {
            log.info("Activación en prepago exitosa! {}", json.get("message"));
            response = StageStatus.SUCCESS;
            log.info(response.toString());
        }

        migService.updateStepByStep(new MigrationOrderEntity(buildRequestActivePrep(req, new PrepaidServicesRequest(0, 0, 0)).toString(), prepaidActive,
                req.getAssessorParams().getOrderId(), response, StageProcess.ACTIVATE_PREPAID, OrderStatus.IN_PROGRESS));
        log.info("ir al siguiente paso enviar encabezado factura a ODA");
        responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.ACTIVATE_PREPAID.toString(), "Alta en prepago", response.equals(StageStatus.SUCCESS) ? "Activación en prepago exitosa!" : "Error de activación en prepago"));
        if (response.equals(StageStatus.SUCCESS)) {
            stepSendHeaderODA(req);
        } else {
            throw new FlowMigrationException("Error al enviar el alta a prepago");
        }
        return response.toString();
    }

    @Override
    public String stepDropPrepaidSoapC(ResponseClientDataDTO req) throws CRMPerException {
        log.info("iniciando baja servicio prepago soap client");
        String dropPrepaid = reqWS.dropPrepaid(buildRequestDropPrep(req));
        log.info("Response del servicio de baja en prepago para híbridos {}", dropPrepaid);
        StageStatus response = StageStatus.FAILED;
        if (dropPrepaid.contains("<return>0</return>")) {
            log.info("Baja prepago soap client exitosa!");
            response = StageStatus.SUCCESS;
        }

        DeleteResponse delRes = new DeleteResponse();
        delRes.setDetailResponse(response.toString());
        delRes.setResponse(response.equals(StageStatus.SUCCESS) ? 0 : 3);
        responseFormat(delRes);
        migService.updateStepByStep(new MigrationOrderEntity(buildRequestDropPrep(req).toString(), dropPrepaid.toString(),
                req.getData().getOrderNumber(), response, StageProcess.DROP_PREPAID, OrderStatus.IN_PROGRESS));
        responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.DROP_PREPAID.toString(),
                "Baja en prepago", response.equals(StageStatus.SUCCESS) ? "Baja en prepago exitosa!" : "Error de baja en prepago"));
        if (response.equals(StageStatus.SUCCESS)) {
            log.info("ir al siguiente paso, alta en prepago soap client");
            stepActivatePrepaid(req);
        } else {
            throw new FlowMigrationException("Error al enviar la baja en prepago");
        }
        return response.toString();
    }

    @Override
    public String stepDropPrepaidWS(ResponseClientDataDTO req) throws CRMPerException {
        log.info("iniciando baja servicio prepago...");
        //TO DO
        //Verificar los parametros utilizados en la baja de prepago para crear el request adecuado desde back y en el front también
        DeleteResponse dropPrepaid = reqWS.dropPrepaidWSimport(buildRequestDropPrep(req));
        log.info("Response de baja en prepago {}", dropPrepaid);
        StageStatus response = StageStatus.FAILED;
        String responseDrope = null;
        if (dropPrepaid.getResponse() == 0) {
            log.info("Baja en prepago exitosa!");
            response = StageStatus.SUCCESS;
        }
        responseDrope = responseFormat(dropPrepaid);
        migService.updateStepByStep(new MigrationOrderEntity(buildRequestDropPrep(req).toString(), responseDrope,
                req.getData().getOrderNumber(), response, StageProcess.DROP_PREPAID, OrderStatus.IN_PROGRESS));
        responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.DROP_PREPAID.toString(),
                "Baja en prepago", response.equals(StageStatus.SUCCESS) ? "Baja en prepago exitosa!" : "Error de baja en prepago"));
        if (response.equals(StageStatus.SUCCESS)) {
            log.info("ir al siguiente paso dar alta en prepago con el nuevo perfil");
            stepActivatePrepaid(req);
        } else {
            throw new FlowMigrationException("Error al enviar el alta a prepago");
        }
        return response.toString();
    }

    private static String responseFormat(DeleteResponse dropPrepaid) {
        return String.format("{\n" +
                "  \"hasError\": \"FALSE\",\n" +
                "  \"responseType\": \"WS_REQUESTS\",\n" +
                "  \"status\": \"SUCCESS\",\n" +
                "  \"data\": {\n" +
                "    \"borrado\": {\n" +
                "      \"detailResponse\": \"%s\",\n" +
                "      \"response\": %s\n" +
                "    }\n" +
                "  }\n" +
                "}", dropPrepaid.getDetailResponse(), dropPrepaid.getResponse());
    }

    @Override
    public String stepSendHeaderODA(ResponseClientDataDTO req) throws CRMPerException {
        log.info("Iniciando envío de encabezado a ODA...");
        //String headerODA = headerODAResponse;//desarrollo
        String headerODA = reqWS.headerODARecharge(buildRequestODAHeader(req));// Producción
        log.info("Response de encabezado a ODA {}", headerODA);
        StageStatus response = StageStatus.FAILED;
        Map<String, Object> json = jsonMapper(headerODA, null);

        log.info("El encabezado fue enviado a ODA exitosamete!");
        json = jsonMapper(headerODA, "code");
        if (json.get("hasError").equals("false")) {
            response = StageStatus.SUCCESS;
            odaCode = json.get("code").toString();
            log.info("Código de ODA {}", odaCode);
        }
        migService.updateStepByStep(new MigrationOrderEntity(buildRequestODAHeader(req).toString(), headerODA,
                req.getData().getOrderNumber(), response, StageProcess.HEADER_ODA, OrderStatus.IN_PROGRESS, odaCode));
        log.info("ir al siguiente paso dar alta en prepago con el nuevo perfil");
        responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.HEADER_ODA.toString(), "Envío de encabezado de factura a ODA", response.equals(StageStatus.SUCCESS) ? "Encabezado enviado a ODA exitoso!" : "Error de envío en encabezado!"));
        if (response.equals(StageStatus.SUCCESS)) {

            stepSendDetailODA(req);
        } else {
            throw new FlowMigrationException("Error al enviar el encabezado a ODA");
        }
        return response.toString();
    }

    @Override
    public String stepSendDetailODA(ResponseClientDataDTO req) throws CRMPerException {
        log.info("Iniciando el envío de detalle a ODA...");
        //String detailODA = headerODAResponse;//desarrollo
        String detailODA = reqWS.detailODARecharge(buildRequestODADetail(req)); //producción
        log.info("Response de detalle a ODA {}", detailODA);
        StageStatus response = StageStatus.FAILED;

        log.info("envío de detalle a ODA exitoso!");
        Map<String, Object> json = jsonMapper(detailODA, null);
        if (json.get("hasError").equals("false")) {
            response = StageStatus.SUCCESS;
        }

        migService.updateStepByStep(new MigrationOrderEntity(buildRequestODADetail(req).toString(), detailODA, req.getData().getOrderNumber(),
                response, StageProcess.DETAIL_ODA,
                response.equals(StageStatus.SUCCESS) ? OrderStatus.COMPLETED : OrderStatus.IN_PROGRESS));
        log.info("fin del proceso de migración!");
        responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.DETAIL_ODA.toString(), "Envío de detalle de factura a ODA", response.equals(StageStatus.SUCCESS) ? "Detalle de factura enviado a ODA exitosamente!" : "Error de envío en detalle factura!"));
        if (response.equals(StageStatus.SUCCESS)) {
            Map<String, Object> odaMap = new HashMap<>();
            odaMap.put("odaCode", odaCode);
            responseStagedDTO.put(count++, saveFlowReturnObject(StageProcess.FINISHED_FLOW.toString(), "completed", true));
            responseStagedDTO.put(count++, odaMap);
        } else {
            throw new FlowMigrationException("Error al enviar el detalle a ODA");
        }
        return response.toString();
    }

    public static Map<String, Object> jsonMapper(String jsonToMap, String containsKey) throws CRMPerException {
        Map<String, Object> mappedJson = new HashMap<>();
        if (jsonToMap.contains("\"hasError\":false") || containsKey != null) {
            try {
                JSONObject json = new JSONObject(jsonToMap);
                mappedJson.put("hasError", json.getString("hasError"));
                mappedJson.put("message", json.getString("message"));
                if (containsKey != null) {
                    mappedJson.put(containsKey, json.getString(containsKey));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                JSONObject json = new JSONObject(jsonToMap);
                mappedJson.put("hasError", json.getString("hasError"));
                mappedJson.put("message", json.getString("message"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return mappedJson;
    }

    public static Map<String, Object> saveFlowReturnObject(String process, String status, Object obj) {
        Map<String, Object> dataReturned = new HashMap<>();
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put(status, obj);
        dataReturned.put(process, objectMap);
        return dataReturned;
    }

    public static boolean isDayAfter(LocalDateTime dateValidate, LocalDateTime dateMatcher) {

        if (dateMatcher.isAfter(dateValidate.plusDays(1L))) {
            return true;
        }
        return false;
    }

}
