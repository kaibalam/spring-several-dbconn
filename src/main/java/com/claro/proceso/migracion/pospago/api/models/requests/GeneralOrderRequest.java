package com.claro.proceso.migracion.pospago.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class GeneralOrderRequest {
    private DropPostPaidRequest dropPost;
    private DropValidationRequest validPost;
    private ActivePrepaidRequest activePre;
    private HeaderODARechRequest headerODA;
    private DetailODARechRequest detailODA;
    private AssessorsOrderDetailRequest assessorDetail;
    private DropPrepaidRequest prepDrop;

}
