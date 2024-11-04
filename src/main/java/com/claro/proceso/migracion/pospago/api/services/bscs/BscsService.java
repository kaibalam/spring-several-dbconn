package com.claro.proceso.migracion.pospago.api.services.bscs;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.AgreementDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ImeiImsiDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.MigrationMotivesDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.RechargeAmountDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.RegionsFromDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.bscs.request.MigrationReasonRequest;

import java.util.List;
import java.util.Map;

public interface BscsService {

    ClientDataDTO findByPhoneNumber(String query) throws CRMPerException;
    ImeiImsiDTO findImeiImsiByMsisdn(String msisdn) throws CRMPerException;
    AgreementDTO getAgreement(Integer customerId) throws CRMPerException;
    String isBlocked(Integer contract) throws CRMPerException;
    String expirationDate() throws CRMPerException;
    List<MigrationMotivesDTO> getMotivos(String msisdn) throws CRMPerException;
    List<RegionsFromDTO> getRegiones(String msisdn) throws CRMPerException;
    void setMigrationReason(ResponseClientDataDTO request) throws CRMPerException;
    List<RechargeAmountDTO> getRecargas(String msisdn) throws CRMPerException;
    Map<String, Boolean> isValidOption(ResponseClientDataDTO data) throws CRMPerException;
}
