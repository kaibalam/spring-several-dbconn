package com.claro.proceso.migracion.pospago.api.models.bscs.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ImeiImsiDTO {
    private String imei;
    private String imsi;
}
