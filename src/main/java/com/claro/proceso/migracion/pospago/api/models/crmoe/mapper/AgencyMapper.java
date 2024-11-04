package com.claro.proceso.migracion.pospago.api.models.crmoe.mapper;


import com.claro.proceso.migracion.pospago.api.models.crmoe.dtos.AgencyDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgencyMapper implements RowMapper<AgencyDTO> {
    @Override
    public AgencyDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

        AgencyDTO dto = new AgencyDTO();
        dto.setUserId(rs.getInt("user_id"));
        dto.setUserAccount(rs.getString("user_account"));
        dto.setAgencia(rs.getString("name"));
        dto.setUiInventoryCode(rs.getString("ui_inventory_code"));
        return dto;
    }
}
