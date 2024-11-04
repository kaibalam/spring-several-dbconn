package com.claro.proceso.migracion.pospago.api.services.bscs.impl;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.AgreementDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ImeiImsiDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.MigrationMotivesDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.RechargeAmountDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.RegionsFromDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.mapper.AgreementMapper;
import com.claro.proceso.migracion.pospago.api.models.bscs.mapper.ClientMapper;
import com.claro.proceso.migracion.pospago.api.models.bscs.mapper.ImeiImsiMapper;
import com.claro.proceso.migracion.pospago.api.models.bscs.mapper.MigrationMotivesMapper;
import com.claro.proceso.migracion.pospago.api.models.bscs.mapper.RegionsFromMapper;
import com.claro.proceso.migracion.pospago.api.models.responses.ResourceNotValidException;
import com.claro.proceso.migracion.pospago.api.services.bscs.BscsService;
import com.claro.proceso.migracion.pospago.api.services.mactpre.ParamsService;
import com.claro.proceso.migracion.pospago.api.services.migration.MigrationFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BscsServiceImpl implements BscsService {

    @Autowired
    @Qualifier("bscsJdbcTemplate")
    private JdbcTemplate jdbc;
    @Autowired
    private ParamsService params;
    @Autowired
    private MigrationFlowService wsReq;

    @Override
    public ClientDataDTO findByPhoneNumber(String msisdn) throws CRMPerException {
        log.info("Obteniendo datos del cliente desde bscs...");
        LocalDateTime initTime = LocalDateTime.now();
        int orderId = (int) (new java.util.Date().getTime() / 1000);
        String profile = params.findByDicTag("GDT_MIG_PROFILE_CODE").get().getDicValor();
        String query = params.findByDicTag("GDT_QUERY_CLIENT_DATA").get().getDicValor();
        String urlIntraSAC = params.findByDicTag("GDT_INTRASAC_ENDPOINT").get().getDicValor();
        String request = String.format(query, profile, msisdn);

        //ClientDataDTO clientDataDTO = new ClientDataDTO(); //desarrollo
        ClientDataDTO clientDataDTO = new ClientDataDTO();
        try {
            clientDataDTO = jdbc.queryForObject(request, new ClientMapper());
        } catch (EmptyResultDataAccessException e){
            log.error(" NO se encontraron datos con este número solicitado!");
        }
        if (clientDataDTO.getMsisdn() != null) {
            profile = validateXT(clientDataDTO.getPlan());
            clientDataDTO.setProfileId(profile);
            ImeiImsiDTO imsi = findImeiImsiByMsisdn(msisdn);
            clientDataDTO.setOrderNumber(orderId);
            clientDataDTO.setIntraSacUrl(urlIntraSAC);
            clientDataDTO.setPostpaidType(wsReq.selectMigrationType(msisdn));
            ImeiImsiDTO imsiDTO = findImeiImsiByMsisdn(msisdn);
            clientDataDTO.setImei(imsiDTO.getImei());
            clientDataDTO.setImsi(imsiDTO.getImsi());
            clientDataDTO.setExpirationDate(expirationDate());
        } else {
            clientDataDTO = new ClientDataDTO();
        }
            return clientDataDTO;
    }

    @Override
    public ImeiImsiDTO findImeiImsiByMsisdn(String msisdn) throws CRMPerException {
        String imeiQuery = params.findByDicTag("GDT_GETIMEI_QUERY").get().getDicValor();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMM");
        DateTimeFormatter dtcf = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fecha = dtf.format(LocalDateTime.now());
        String fechaC = dtcf.format(LocalDateTime.now());
        String pfx = "gt";
        String queryImeiImsi = String.format(imeiQuery, pfx, msisdn, fecha, fechaC, pfx, msisdn, fecha, fechaC);
        ImeiImsiDTO res = new ImeiImsiDTO();
        try {
            res = jdbc.queryForObject(queryImeiImsi, new ImeiImsiMapper());
        } catch (Exception e){
            log.info(String.format("No se encontró Imei ni Imsi para el msisdn %s",msisdn));
        }
        return res;
    }

    @Override
    public AgreementDTO getAgreement(Integer customerId) throws CRMPerException {
        String query = params.findByDicTag("GDT_PAYAGREE_QUERY").get().getDicValor();
        String agreementQuery = String.format(query, customerId);
        AgreementDTO dto = new AgreementDTO();
        try {
            dto = jdbc.queryForObject(agreementQuery, new AgreementMapper());
        } catch (Exception e) {
            log.info(" Error = [{}]",e.getMessage());
        }
        return dto;
    }

    @Override
    public String isBlocked(Integer contract) throws CRMPerException {
        String snCode = params.findByDicTag("GDT_BLK_CODES").get().getDicValor();
        String queryBlocked = params.findByDicTag("GDT_BLKCOD_QUERY").get().getDicValor();
        String queryIsBlocked = String.format(queryBlocked, contract, snCode);
        return jdbc.queryForObject(queryIsBlocked, String.class);
    }

    @Override
    public String expirationDate() throws CRMPerException {
        String dateStoreParam = params.findByDicTag("GDT_EXP_DATE").get().getDicValor();
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatted = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        String expirationDate = null;
        String[] values = dateStoreParam.split(",");
        String medida = values[0];
        String cantidad = values[1];
        switch (medida) {
            case "h":
                date = date.plusHours(Integer.parseInt(cantidad));
                break;
            case "m":
                date = date.plusMonths(Integer.parseInt(cantidad));
                break;
            default:
                date = date.plusDays(Integer.parseInt(cantidad));
        }
        expirationDate = date.format(formatted);
        return expirationDate;
    }

    @Override
    public List<MigrationMotivesDTO> getMotivos(String msisdn) throws CRMPerException {
        String queryMotives = params.findByDicTag("GDT_SEARCH_MOTIVOS").get().getDicValor();
        queryMotives = queryMotives.replace("%s", "GUA ");
        List<MigrationMotivesDTO> motivos = jdbc.query(queryMotives, new MigrationMotivesMapper());
        if (CollectionUtils.isEmpty(motivos)) {
            return new ArrayList<>();
        }
        return motivos;
    }

    @Override
    public List<RegionsFromDTO> getRegiones(String msisdn) throws CRMPerException {
        String queryRegions = params.findByDicTag("GDT_SEARCH_REGIONS").get().getDicValor();
        queryRegions = queryRegions.replace("%s", "GUA");
        List<RegionsFromDTO> regiones = jdbc.query(queryRegions, new RegionsFromMapper());
        if (CollectionUtils.isEmpty(regiones)) {
            return new ArrayList<>();
        }
        return regiones;
    }

    @Override
    public void setMigrationReason(ResponseClientDataDTO request) throws CRMPerException {
        String queryUpdateReason = params.findByDicTag("GDT_UPDATE_REASON").get().getDicValor();
        try {
            int updateStatement = jdbc.update(queryUpdateReason, request.getAssessorParams().getReasonCode(), request.getData().getContract());
        } catch (BadSqlGrammarException e){
            throw new ResourceNotValidException(" No se pudo actualizar el la razón debido al siguiente error: "+ e.getCause());
        }
    }

    @Override
    public List<RechargeAmountDTO> getRecargas(String msisdn) throws CRMPerException {
        String queryRechargeList = params.findByDicTag("GDT_REC_TAGS").get().getDicValor();
        String[] recargaArray = queryRechargeList.split(",");
        List<RechargeAmountDTO> listaRecargas = new ArrayList<>();
        for (String tgs : recargaArray) {
            RechargeAmountDTO rechargeTags = new RechargeAmountDTO(tgs, tgs);
            listaRecargas.add(rechargeTags);
        }
        return listaRecargas;
    }

    @Override
    public Map<String, Boolean> isValidOption(ResponseClientDataDTO data) throws CRMPerException {
        Map<String, Boolean> validList = new HashMap<>();
        Boolean saldo = data.getData().getBalance() > 0 ? validList.put("tieneSaldo", true) : validList.put("tieneSaldo", false);
        DateTimeFormatter formateador = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toFormatter();
        LocalDateTime expireDate = LocalDateTime.parse(data.getData().getContractExpiration(), formateador);
        Boolean expiro = expireDate.isBefore(LocalDateTime.now()) ? validList.put("noHaExpirado", false) : validList.put("noHaExpirado", true);
        Boolean bloqueos = data.getActiveBlock().equals("false") ? validList.put("estaBloqueado", false) : validList.put("estaBloqueado", true);
        Boolean convenio = data.getAgreement().getTotalAmount() == null ? validList.put("tieneConvenio", false) : validList.put("tieneConvenio", true);
        Boolean activo = data.getData().getCHistoryStatus().equals("ACTIVO") ? validList.put("noEstaActivo", false) : validList.put("noEstaActivo", true);
        Boolean sinAgencia = data.getData().getAgency().isEmpty() ? validList.put("noTieneAgencia", true) : validList.put("noTieneAgencia", false);
        return validList;
    }

    public  String validateXT(Integer plan) throws CRMPerException {
        //Validar si es XT para cambiarle el perfil
        String planesParam = params.findByDicTag("MIG_XT_PLANS_BUNCH").get().getDicValor();
        String[] planes = planesParam.split(",");
        for (String include: planes) {
            if (include.equals(String.valueOf(plan))){
                log.info("Es XT cambiando perfil");
                return params.findByDicTag("MIG_PROFILE_XT_PLAN").get().getDicValor();
            }
        }
        return params.findByDicTag("GDT_MIG_PROFILE_CODE").get().getDicValor();
    }

}
