package com.claro.proceso.migracion.pospago.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DropPrepaidRequest {
    private String subId;
    private Integer operatorId;
}
