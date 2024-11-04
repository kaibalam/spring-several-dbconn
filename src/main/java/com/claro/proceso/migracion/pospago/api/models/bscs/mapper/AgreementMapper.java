package com.claro.proceso.migracion.pospago.api.models.bscs.mapper;


import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.AgreementDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AgreementMapper implements RowMapper<AgreementDTO> {
    @Override
    public AgreementDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        AgreementDTO dto = new AgreementDTO();
        dto.setCostCenter(rs.getInt("costcenter"));
        dto.setTotalAmount(rs.getDouble("total_amount"));
        dto.setEntDate(rs.getString("ent_date"));
        dto.setModDate(rs.getString("mod_date"));
        dto.setPaRemark(rs.getString("pa_remark"));
        dto.setActivated(rs.getString("activated"));
        dto.setQuotaOne(rs.getDouble("quota1"));
        dto.setQuotaN(rs.getDouble("quota_n"));
        dto.setQuotaNum(rs.getInt("quotanum"));
        dto.setEntUser(rs.getString("ent_user"));
        dto.setModUser(rs.getString("mod_user"));
        return dto;
    }
}
