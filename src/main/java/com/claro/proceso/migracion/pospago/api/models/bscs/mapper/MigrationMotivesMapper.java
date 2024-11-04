package com.claro.proceso.migracion.pospago.api.models.bscs.mapper;

import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.MigrationMotivesDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MigrationMotivesMapper implements RowMapper<MigrationMotivesDTO> {
    @Override
    public MigrationMotivesDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        MigrationMotivesDTO dto = new MigrationMotivesDTO();
        dto.setLabel(rs.getString("rs_desc").substring(4));
        dto.setValue(rs.getString("rs_id"));
        return dto;
    }
}
