package com.claro.proceso.migracion.pospago.api.models.crmoe.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AgencyDTO {
    private Integer userId;
    private String userAccount;
    private String uiInventoryCode;
    private String area;
    private String region;
    private String agencia;
}
