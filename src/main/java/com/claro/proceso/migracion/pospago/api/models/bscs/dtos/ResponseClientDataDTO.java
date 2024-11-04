package com.claro.proceso.migracion.pospago.api.models.bscs.dtos;

import com.claro.proceso.migracion.pospago.api.models.requests.AssessorsOrderDetailRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResponseClientDataDTO {
    private ClientDataDTO data;
    private AgreementDTO agreement;
    private String activeBlock;
    private String notAbleToMigrate;
    private List<String> causesMessage;
    private AssessorsOrderDetailRequest assessorParams;
}
