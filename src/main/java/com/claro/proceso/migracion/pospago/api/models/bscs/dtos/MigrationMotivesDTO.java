package com.claro.proceso.migracion.pospago.api.models.bscs.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MigrationMotivesDTO {
    private String label;
    private String value;
}
