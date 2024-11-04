package com.claro.proceso.migracion.pospago.api.models.mactpre.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParamRequestDTO {
    private Long id;
    private String dicValor;
    private String dicTag;
    private Date fechaAlta;
    private String activo;
    private String pais;
    private String tipo;
    private String desarrollo;
}
