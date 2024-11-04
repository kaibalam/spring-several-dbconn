package com.claro.proceso.migracion.pospago.api.models.crmoe.mapper;

import com.claro.proceso.migracion.pospago.api.models.crmoe.dtos.AgencyDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgencyObjMapper implements RowMapper<AgencyDTO> {
    @Override
    public AgencyDTO mapRow(ResultSet rs, int i) throws SQLException {
        AgencyDTO dto = new AgencyDTO();
        dto.setUserId(rs.getInt("user_id"));
        dto.setUserAccount(rs.getString("user_account"));
        dto.setUiInventoryCode(rs.getString("ui_inventory_code"));
        dto.setArea(rs.getString("area"));
        dto.setRegion(rs.getString("region"));
        dto.setAgencia(rs.getString("agencia"));
        return dto;
    }
}
