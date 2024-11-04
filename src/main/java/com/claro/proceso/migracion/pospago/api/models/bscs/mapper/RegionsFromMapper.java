package com.claro.proceso.migracion.pospago.api.models.bscs.mapper;

import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.RegionsFromDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegionsFromMapper implements RowMapper<RegionsFromDTO> {
    @Override
    public RegionsFromDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        RegionsFromDTO dto = new RegionsFromDTO();
        dto.setLabel(rs.getString("zona"));
        dto.setValue(rs.getString("area_id"));
        return dto;
    }
}
