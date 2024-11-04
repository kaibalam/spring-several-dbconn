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
public class AssessorsOrderDetailRequest {
    private Integer reasonCode;
    private Integer orderId;
    private Double amountProration;
    private String userId;
    private String masterCode;
    private String regionCode;
    private String countryCode;
    private Integer priceRecharge;
    private String settedNit;
    private String odaCode;

}
