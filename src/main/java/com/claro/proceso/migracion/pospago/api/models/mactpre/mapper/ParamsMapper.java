package com.claro.proceso.migracion.pospago.api.models.mactpre.mapper;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.ParamsEntity;
import com.claro.proceso.migracion.pospago.api.models.mactpre.dtos.ParamRequestDTO;

public class ParamsMapper  {
    public ParamsMapper(ParamRequestDTO requestDTO) {
    }

    public ParamsEntity mapRow(ParamRequestDTO rs) {
        ParamsEntity dto = new ParamsEntity();
        dto.setFechaAlta(rs.getFechaAlta());
        dto.setActivo(rs.getActivo());
        dto.setDesarrollo(rs.getDesarrollo());
        dto.setPais(rs.getPais());
        dto.setDicTag(rs.getDicTag());
        dto.setDicValor(rs.getDicValor());
        dto.setTipo(rs.getTipo());

        return dto;
    }
}
