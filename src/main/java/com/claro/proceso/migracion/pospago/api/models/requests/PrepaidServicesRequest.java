package com.claro.proceso.migracion.pospago.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PrepaidServicesRequest {
    private Integer spCode;
    private Integer ndCode;
    private Integer profileId;

    @Override
    public String toString() {
        return "[{  \"spCode\":" + spCode +
                ", \"ndCode\":" + ndCode +
                ", \"profileId\":" + profileId + "}]";
    }
}
