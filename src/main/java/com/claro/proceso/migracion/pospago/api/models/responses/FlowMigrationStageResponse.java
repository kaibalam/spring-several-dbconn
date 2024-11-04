package com.claro.proceso.migracion.pospago.api.models.responses;

import com.claro.proceso.migracion.pospago.api.models.enums.StageProcess;
import com.claro.proceso.migracion.pospago.api.models.enums.StageStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FlowMigrationStageResponse {
    private boolean hasError;
    private String message;
    private StageProcess stageProcess;
    private StageStatus stageStatus;
    private Object data;
}
