package com.claro.proceso.migracion.pospago.api.services.wsrequests;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.requests.ActivePrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DetailODARechRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPostPaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropValidationRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.HeaderODARechRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.MasterCodeRequest;
import com.claro.proceso.migracion.pospago.api.wsdl.DeleteResponse;

public interface WebRequestService {

    String validateMasterCode(MasterCodeRequest request) throws CRMPerException;
    String dropPostpaid(DropPostPaidRequest request) throws CRMPerException;
    String dropVerification(DropValidationRequest request) throws CRMPerException;
    String cleanCacheSPR(String msisdn) throws CRMPerException;
    String queryPostpaidTypeSPR(String msisdn) throws CRMPerException;
    String activatePrepaid(ActivePrepaidRequest msisdn, String type) throws CRMPerException;
    String dropPrepaid(DropPrepaidRequest request) throws CRMPerException;
    DeleteResponse dropPrepaidWSimport(DropPrepaidRequest request) throws CRMPerException;
    String headerODARecharge(HeaderODARechRequest req) throws CRMPerException;
    String detailODARecharge(DetailODARechRequest req) throws CRMPerException;
}
