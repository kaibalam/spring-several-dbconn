package com.claro.proceso.migracion.pospago.api.controllers;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.request.DataClientRequest;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;
import com.claro.proceso.migracion.pospago.api.services.bscs.BscsService;
import com.claro.proceso.migracion.pospago.api.services.crmoe.CrmOeService;
import com.claro.proceso.migracion.pospago.api.services.wsrequests.WebRequestService;
import com.claro.proceso.migracion.pospago.api.utils.ApiResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/request-gather-bscs8")
@CrossOrigin(origins = {"http://localhost:5173"})
@Slf4j
public class BscsDataGatherController {

    @Autowired
    private BscsService bscs;
    @Autowired
    private CrmOeService crmoe;
    @Autowired
    private WebRequestService ws;

    @PostMapping("/datosCliente")
    public ResponseEntity<ApiResponse> getDataClient(@Validated @RequestBody DataClientRequest request) throws CRMPerException {
        log.info("Obtener datos del cliente");
        if (request.getUserName() == null || request.getPhone() == null) {
            return ResponseEntity.of(java.util.Optional.of(
                    ApiResponseUtils.success(
                            ApiResponseType.PARAMS, "datos-cliente-lacking-error", "Falta un valor en la solicitud enviada ")));
        } else {
            /*ClientDataDTO clientDataDTO = new ClientDataDTO(request.getPhone(),"Ricardo","Valenzuela",421234,123422,4321234,"ACTIVO","07/01/2019",0.0,210,6,"DPI",
                    "2319765850101","11/26/1982","M","MIG PROFILE","Guatemala","eje 4 casa 14 sector encinos","Planes de barcenas Villanueva","63003295","123498765234212",
                    "1234568789434537","8","121234212443","Huawey","Y6","05/04/2024","G201",88423851,"www.google.com","2023-12-23 14:00:21","HIBRIDO"); //desarrollo*/
            if (!request.getPhone().startsWith("502") && request.getPhone().length() == 8) {
                request.setPhone("502" + request.getPhone());
            }
            ClientDataDTO clientDataDTO = bscs.findByPhoneNumber(request.getPhone()); //produccion
            ResponseClientDataDTO resp = new ResponseClientDataDTO();
            List<String> messageList = new ArrayList<>();
            if (clientDataDTO.getMsisdn() != null) {
                clientDataDTO.setAgency(crmoe.getAgency(request.getUserName())); //produccion
                resp.setData(clientDataDTO);
                resp.setAgreement(bscs.getAgreement(clientDataDTO.getClientId()));//produccion
                //resp.setAgreement(new AgreementDTO(124,12.12,"asdf","sd","as","sd",12.2,32.3,123,"","")); //desarrollo
                //resp.setAgreement(new AgreementDTO(null,null,null,null,null,null,null,null,null,null,null)); //desarrollo
                resp.setActiveBlock(bscs.isBlocked(clientDataDTO.getContract()));//produccion
                //resp.setActiveBlock("false"); //desarrollo
                clientDataDTO.setAgencyObjectDTO(crmoe.getAgencyObject(request.getUserName()));
                Map<String, Boolean> able = bscs.isValidOption(resp);
                AtomicBoolean isNotAble = new AtomicBoolean(false);
                able.forEach((e, i) -> {
                    if (i.equals(true)) {
                        log.info("No es posible migrar porque {}", e);
                        isNotAble.set(true);
                        messageList.add(e);
                    }
                });
                resp.setNotAbleToMigrate(isNotAble.toString());
                resp.setCausesMessage(messageList);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtils
                        .error(ApiResponseType.QUERIES, "200", String.format("No se encontraron datos con este número solicitado [ %s ]", request.getPhone())));
            }
            return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES, "cliente",
                    resp));
        }
    }

    @PostMapping("/motivos")
    public ResponseEntity<ApiResponse> getMotivos(@Validated @RequestBody String msisdn) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES, "motivos",
                bscs.getMotivos(msisdn)));
    }

    @PostMapping("/regiones")
    public ResponseEntity<ApiResponse> getRegiones(@Validated @RequestBody String msisdn) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES, "regiones",
                bscs.getRegiones(msisdn)));
    }

    @PostMapping("/recargas")
    public ResponseEntity<ApiResponse> getRecargas(@Validated @RequestBody String msisdn) throws CRMPerException {
        return ResponseEntity.ok(ApiResponseUtils.success(ApiResponseType.QUERIES, "recarga",
                bscs.getRecargas(msisdn)));
    }
}
