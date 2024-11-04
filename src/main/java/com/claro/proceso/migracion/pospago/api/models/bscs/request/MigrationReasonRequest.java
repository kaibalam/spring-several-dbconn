package com.claro.proceso.migracion.pospago.api.models.bscs.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MigrationReasonRequest {
    private String reasonCode;
    private Integer contract;
}
