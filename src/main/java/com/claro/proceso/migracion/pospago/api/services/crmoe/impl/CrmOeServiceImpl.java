package com.claro.proceso.migracion.pospago.api.services.crmoe.impl;

import com.claro.proceso.migracion.pospago.api.models.CRMPerException;
import com.claro.proceso.migracion.pospago.api.models.crmoe.dtos.AgencyDTO;
import com.claro.proceso.migracion.pospago.api.models.crmoe.mapper.AgencyMapper;
import com.claro.proceso.migracion.pospago.api.models.crmoe.mapper.AgencyObjMapper;
import com.claro.proceso.migracion.pospago.api.services.crmoe.CrmOeService;
import com.claro.proceso.migracion.pospago.api.services.mactpre.ParamsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CrmOeServiceImpl implements CrmOeService {
    @Autowired
    @Qualifier("crmoeJdbcTemplate")
    private JdbcTemplate jdbc;
    @Autowired
    private ParamsService params;

    @Override
    public String getAgency(String userName) throws CRMPerException {
        String query = params.findByDicTag("GDT_GETAGNY_QUERY").get().getDicValor();
        String queryAgency = String.format(query, userName);
        AgencyDTO agencyDTO = new AgencyDTO();
        try {
            agencyDTO = jdbc.queryForObject(queryAgency, new AgencyMapper());
        } catch (Exception e){
            log.error("No se obtuvo ningún dato de agencia {}", e.getMessage());
        }
        return agencyDTO.getUiInventoryCode() == null? "" : agencyDTO.getUiInventoryCode();
    }

    @Override
    public AgencyDTO getAgencyObject(String userName) throws CRMPerException {
        String query = params.findByDicTag("GDT_GETAGNY_QUERY_OBJ").get().getDicValor();
        String queryAgency = String.format(query, userName);
        String user = userName.replace("CLAROGT.","");
        queryAgency = queryAgency.replace(userName,"%"+user+"%");
        AgencyDTO agencyDTO = new AgencyDTO();
        try {
            agencyDTO = jdbc.queryForObject(queryAgency, new AgencyObjMapper());
        } catch (Exception e){
            log.error("No se obtuvo ningún dato de agencia {}", e.getMessage());
        }
        return agencyDTO;
    }

}
