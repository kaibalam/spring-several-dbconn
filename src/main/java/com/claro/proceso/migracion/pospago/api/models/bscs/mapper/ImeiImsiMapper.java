package com.claro.proceso.migracion.pospago.api.models.bscs.mapper;


import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ImeiImsiDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ImeiImsiMapper implements RowMapper<ImeiImsiDTO> {
    @Override
    public ImeiImsiDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ImeiImsiDTO dto = new ImeiImsiDTO();
        dto.setImei(rs.getString("imei"));
        dto.setImsi(rs.getString("imsi"));

        return dto;
    }
}
