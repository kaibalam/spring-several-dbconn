package com.claro.proceso.migracion.pospago.api.services.wsrequests.impl;

import com.claro.proceso.migracion.pospago.api.config.SoapClient;
import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.requests.ActivePrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DetailODARechRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPostPaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropValidationRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.HeaderODARechRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.MasterCodeRequest;
import com.claro.proceso.migracion.pospago.api.models.responses.EncodingNotSupportedExcpetion;
import com.claro.proceso.migracion.pospago.api.models.responses.ErrorWebServiceConnectionRefusedException;
import com.claro.proceso.migracion.pospago.api.models.responses.ResourceNotFoundException;
import com.claro.proceso.migracion.pospago.api.services.mactpre.ParamsService;
import com.claro.proceso.migracion.pospago.api.services.wsrequests.WebRequestService;
import com.claro.proceso.migracion.pospago.api.utils.ErrorHandleMessages;
import com.claro.proceso.migracion.pospago.api.wsdl.DeleteResponse;
import com.claro.proceso.migracion.pospago.api.wsdl.DeleteSubscriberAttr;
import com.claro.proceso.migracion.pospago.api.wsdl.ServicioWs;
import com.claro.proceso.migracion.pospago.api.wsdl.ServiciosWsImplService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.ws.rs.core.MediaType;
import javax.xml.ws.WebServiceException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WebRequestsServicesImpl implements WebRequestService {
    @Autowired
    private ParamsService params;

    @Override
    public String validateMasterCode(MasterCodeRequest request) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado del servicio del código maestro {}",request.getCode());
        String masterCodeToken = params.findByDicTag("MASTER_CODE_TOKEN").get().getDicValor();
        String masterCodeUrl = params.findByDicTag("GDT_WS_VALIDATE_MCODE").get().getDicValor();
        String content = String.format("{\"MASTER_CODE\":\"%s\",\"COUNTRY_CODE\":\"%s\"}", request.getCode(), request.getCountryCode());
        try {
            String encodeContent = URLEncoder.encode(content, "UTF-8");
            Client client = Client.create();
            WebResource resource = client.resource(masterCodeUrl)
                    .queryParam("DYNAMIC_QUERY_CODE", "MASTER_CODES")
                    .queryParam("DYNAMIC_QUERY_NUMBER", "3")
                    .queryParam("Content", encodeContent);
            ClientResponse response = resource.accept("application/json").header("Authorization", masterCodeToken).get(ClientResponse.class);
            return response.getEntity(String.class);
        } catch (ClientHandlerException | UnsupportedEncodingException e) {
            if (e.getMessage().contains("Connection timed out: connect")) {
                throw new WebServiceException(" No hay conexión hacia el servicio solicitado: \"consulta código maestro\"");
            }
            throw new EncodingNotSupportedExcpetion("El encode efectuado no es válido");
        }
    }

    @Override
    public String dropPostpaid(DropPostPaidRequest request) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado del servicio de baja en pospago");
        StringBuilder reqDrop = new StringBuilder();
        String urlDrop = params.findByDicTag("GDT_WS_DROP_POSIPAID").get().getDicValor();
        reqDrop.append("{\"country\":").append(request.getCountry()).append(",")
                .append("\"contractId\":").append(request.getContractId()).append("}");
        if (request.getAmountProration() == null || request.getAmountProration() <= 0) {
            throw new ResourceNotFoundException(String.format(ErrorHandleMessages.ERROR_PARMAM_IS_MISSING_OR_INVALID_VALUE, request.getAmountProration()));
        }
        try {
            Client client = Client.create();
            WebResource resource = client.resource(urlDrop);
            ClientResponse resp = resource.accept("application/json").type(MediaType.APPLICATION_JSON).put(ClientResponse.class, reqDrop.toString());
            return resp.getEntity(String.class);
        } catch (ClientHandlerException e) {
            throw new ErrorWebServiceConnectionRefusedException(" El servicio de baja pospago no esta respondiendo: " + e.getMessage());
        }
    }

    @Override
    public String dropVerification(DropValidationRequest request) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado del servicio de baja en pospago...");
        String urlDropVerify = params.findByDicTag("GDT_NOTIFYDOWN_WS").get().getDicValor();
        String attempsParam = params.findByDicTag("GDT_ATTEMP_NUM_VERIFY").get().getDicValor();
        String timeToRetry = params.findByDicTag("GDT_ATTEMP_TIME_VERIFY").get().getDicValor();
        String entity = null;
        boolean validator = true;
        Long nextAttemTry = Long.parseLong(timeToRetry);
        log.info("Datos cargados");
        ObjectMapper map = new ObjectMapper();
        try {
            String reqDropValidMapper = map.writerWithDefaultPrettyPrinter().writeValueAsString(request);
            log.info("request Drop validation: {}", reqDropValidMapper);
            int count = 0;
            entity = cycleValidator(urlDropVerify, attempsParam, validator, nextAttemTry, reqDropValidMapper, count);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return entity;
    }

    @Override
    public String cleanCacheSPR(String msisdn) throws CRMPerException {
        log.info("WebRequest limpiando el cache en SPR...");
        String urlCleanCache = params.findByDicTag("URL_CLN_CACHE_SPR").get().getDicValor();
        String paramPass = params.findByDicTag("PASS_SPR_CLN_CACHE").get().getDicValor();
        String paramFlow = params.findByDicTag("FLOW_SPR_CLN_CACHE").get().getDicValor();
        try {

            Client client = Client.create();
            WebResource resource = client.resource(urlCleanCache);
            ClientResponse resp = resource
                    .queryParam("password", paramPass).queryParam("key", msisdn).queryParam("flow", paramFlow)
                    .accept("application/json").type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return resp.getEntity(String.class);
        } catch (ClientHandlerException e) {
            throw new ErrorWebServiceConnectionRefusedException(" Se obtuvo un error al conectar el servicio de limpieza de SPR cache: " + e.getMessage());
        }
    }

    @Override
        public String queryPostpaidTypeSPR(String msisdn) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado en SPR...");
        String urlQueryPostType = params.findByDicTag("URL_SPR_QUERY_POST_TYPE").get().getDicValor();
        String paramAgent = params.findByDicTag("AGENT_SPR_QUERY_POST").get().getDicValor();
        String paramService = params.findByDicTag("SERVICE_SPR_QUERY_POST").get().getDicValor();
        try {
            Client client = Client.create();
            WebResource resource = client.resource(urlQueryPostType);
            ClientResponse resp = resource.queryParam("key", msisdn).queryParam("agent", paramAgent).queryParam("service", paramService)
                    .accept("application/json").type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            return resp.getEntity(String.class);
        } catch (ClientHandlerException e) {
            throw new ErrorWebServiceConnectionRefusedException(" Se obtuvo un error al consultar servicio de SPR: " + e.getMessage());
        }
    }

    @Override
    public String activatePrepaid(ActivePrepaidRequest req, String type) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado en activación de msisdn en prepago...");
        log.info(req.toString());
        String urlActivatePrepaid = params.findByDicTag("GDT_WS_ACTIVE_PREPAID").get().getDicValor();
        String activationType = params.findByDicTag("GDT_ACTIVATION_TYPE").get().getDicValor();
        String[] types = activationType.split(",");
        activationType = type.equals("HIBRIDO") ? types[1] : types[0];
        req.setActivationType(activationType);
        try {
            Client client = Client.create();
            WebResource resource = client.resource(urlActivatePrepaid);
            ClientResponse resp = resource.accept("application/json").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, req.toString());
            return resp.getEntity(String.class);
        } catch (Exception e) {
            throw new ErrorWebServiceConnectionRefusedException(" Error al conectar el servicio activación prepago: " + e.getMessage());
        }
    }

    @Override
    public String dropPrepaid(DropPrepaidRequest req) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado en baja de msisdn en prepago...");
        //TODO Crer estos dos parametros antes de hacer las pruebas en producción
        String urlDropPrepaid = params.findByDicTag("URL_DROP_PREPAID_WS").get().getDicValor();
        String xmlDropRequest = params.findByDicTag("XML_DROP_PREPAID_REQ").get().getDicValor();
        //urlDropPrepaid = "http://localhost:8082/webapp-jaxws/ServiciosWsImpl?wsdl";
        xmlDropRequest = String.format(xmlDropRequest,req.getSubId());
        log.info("XML enviado en el request {}", xmlDropRequest);
        try {
            return new SoapClient().consumeTheService(xmlDropRequest, "deleteSubscriber", urlDropPrepaid);
        } catch (WebServiceException e) {
            throw new ErrorWebServiceConnectionRefusedException(" Se obturo un error al consultar el servicio de baja en prepago: " + e.getCause());
        }
    }

    @Override
    public DeleteResponse dropPrepaidWSimport(DropPrepaidRequest req) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado en baja de msisdn en prepago...");
        try {
            ServicioWs servicio = new ServiciosWsImplService().getServiciosWsImplPort();
            DeleteSubscriberAttr subscriberAttr = new DeleteSubscriberAttr();
            subscriberAttr.setOperationId(req.getOperatorId());
            subscriberAttr.setSubId(req.getSubId());
            log.info("respuesta del servicio {}", servicio.deleteSubscriber(subscriberAttr).getDetailResponse());
            return servicio.deleteSubscriber(subscriberAttr);
        } catch (WebServiceException e) {
            throw new ErrorWebServiceConnectionRefusedException(" Se obtuvo un error al consultar servicio de baja en prepago wsimport: " + e.getMessage());
        }
    }

    @Override
    public String headerODARecharge(HeaderODARechRequest req) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado en encabezado de ODA...");
        String urlHeaderODAEndpoint = params.findByDicTag("GDT_WSPATH_HEADER_ODA").get().getDicValor();
        log.info(req.toString());
        try {
            Client client = Client.create();
            WebResource resource = client.resource(urlHeaderODAEndpoint);
            ClientResponse resp = resource.accept("application/json").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, req.toString());
            return resp.getEntity(String.class);
        } catch (ClientHandlerException e) {
            throw new ErrorWebServiceConnectionRefusedException(" Se obtuvo un error al consultar el servicio de encabezado de ODA: "+e.getMessage());
        }
    }

    @Override
    public String detailODARecharge(DetailODARechRequest req) throws CRMPerException {
        log.info("WebRequest Obteniendo resultado en detalle de ODA...");
        String urlDetailODAEndpoint = params.findByDicTag("GDT_WSPATH_DETAIL_ODA").get().getDicValor();
        log.info(req.toString());
        try {
            Client client = Client.create();
            WebResource resource = client.resource(urlDetailODAEndpoint);
            ClientResponse resp = resource.accept("application/json").type(MediaType.APPLICATION_JSON).post(ClientResponse.class, req.toString());
            return resp.getEntity(String.class);
        } catch (ClientHandlerException e){
            throw new ErrorWebServiceConnectionRefusedException(" Se obtuvo un error al consultar el servicio de detalle a ODA: "+e.getMessage());
        }
    }

    private static String cycleValidator(String urlDropVerify, String attempsParam, boolean validator, Long nextAttemTry, String reqDropValidMapper, int count) {
        String entity;
        boolean hasError;
        do {
            log.info(String.format("loop de validación ha iniciado condator = %s", count));
            try {
                Client client = Client.create();
                WebResource resource = client.resource(urlDropVerify);
                ClientResponse resp = resource.accept("application/json").type(MediaType.APPLICATION_JSON).put(ClientResponse.class, reqDropValidMapper);
                log.info(String.format("Se ha consultado el estado de la baja response status = %s", resp.getStatus()));
                entity = resp.getEntity(String.class);
                log.info("Response de la validación de la baja en el ciclo {}",entity);
            } catch (ClientHandlerException e) {
                throw new ErrorWebServiceConnectionRefusedException(" Se obtuvo un error al conectar el servicio de validación de baja: " + e.getMessage());
            }
            JSONObject jsonResponse;
            try {
                jsonResponse = new JSONObject(entity);
                hasError = jsonResponse.getBoolean("hasError");
                if (!hasError) {
                    validator = false;
                    log.info(String.format("respuesta de la valicación exitosa, hasError = %s, y validador seteado a %s", hasError, validator));
                } else {
                    count++;
                    if (count == Integer.parseInt(attempsParam)) {
                        validator = false;
                    }
                    timeWaiting(attempsParam, nextAttemTry);
                    log.info(String.format("Se obtuvo respuesta del servicio hasError = %s, y contador de intentos igual a %s", hasError, count));
                }
            } catch (JSONException ex) {
                log.error("Error en el parseo de json: {0}", ex);
            }
        } while (validator);
        return entity;
    }

    private static void timeWaiting(String attempsParam, Long nextAttemTry) {
        try {
            TimeUnit.SECONDS.sleep(nextAttemTry);
            log.info(String.format("Siempo de espera para reintento = %s, cantidad de reintentos = %s", nextAttemTry, attempsParam));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
