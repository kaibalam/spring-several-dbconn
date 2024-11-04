package com.claro.proceso.migracion.pospago.api.models.bscs.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AgreementDTO {
    private Integer costCenter;
    private Double totalAmount;
    private String entDate;
    private String modDate;
    private String paRemark;
    private String activated;
    private Double quotaOne;
    private Double quotaN;
    private Integer quotaNum;
    private String entUser;
    private String modUser;
}
