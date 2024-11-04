package com.claro.proceso.migracion.pospago.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DropValidationRequest {
    private Integer orderNumber;
    private String userId;
    private Integer contractId;
    private String phoneNumber;
    private String sim;

    @Override
    public String toString() {
        return "{\"orderNumber\": "+ orderNumber +
                ", \"userId\": \"" + userId + '\"' +
                ", \"contractId\":" + contractId +
                ", \"phoneNumber\": \"" + phoneNumber + '\"' +
                ", \"sim\":\"" + sim + '\"' +
                '}';
    }
}
