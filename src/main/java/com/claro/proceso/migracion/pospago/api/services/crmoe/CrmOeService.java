package com.claro.proceso.migracion.pospago.api.services.crmoe;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.crmoe.dtos.AgencyDTO;

public interface CrmOeService {
    String getAgency(String userName) throws CRMPerException;

    AgencyDTO getAgencyObject(String userName) throws  CRMPerException;
}
