package com.claro.proceso.migracion.pospago.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ActivePrepaidRequest {
    private String portbNumber;
    private Integer orderId;
    private String  salesManCode;
    private String  phoneNumber;
    private String  sim;
    private String  imei;
    private String  brand;
    private String  model;
    private String  activationType;
    private Integer  documentType;
    private String  documentNumber;
    private String  documentExpirationDate;
    private String  customerFirstname;
    private String  customerLastname;
    private String  customerAddress;
    private String  region;
    private String  customerBirthday;
    private String  titleSex;
    private String  userId;
    private String  dist;
    private String  profile;
    private String  promoId;
    private String  channel;
    private Integer  registerInfo;
    private String  otherValue;
    private String  codePortability;
    private PrepaidServicesRequest prepaidServices;

    @Override
    public String toString() {
        return "{\"portbNumber\": \"" + portbNumber + '\"' +
                ", \"orderId\":" + orderId +
                ", \"salesManCode\":\"" + salesManCode + '\"' +
                ", \"phoneNumber\":\"" + phoneNumber + '\"' +
                ", \"sim\":\"" + sim + '\"' +
                ", \"imei\":\"" + imei + '\"' +
                ", \"brand\":\"" + brand + '\"' +
                ", \"model\":\"" + model + '\"' +
                ", \"activationType\":\"" + activationType + '\"' +
                ", \"documentType\":\"" + documentType + '\"' +
                ", \"documentNumber\":\"" + documentNumber + '\"' +
                ", \"documentExpirationDate\":\"" + documentExpirationDate + '\"' +
                ", \"customerFirstname\":\"" + customerFirstname + '\"' +
                ", \"customerLastname\":\"" + customerLastname + '\"' +
                ", \"customerAddress\":\"" + customerAddress + '\"' +
                ", \"region\":\"" + region + '\"' +
                ", \"customerBirthday\":\"" + customerBirthday + '\"' +
                ", \"titleSex\":\"" + titleSex + '\"' +
                ", \"userId\":\"" + userId + '\"' +
                ", \"dist\":\"" + dist + '\"' +
                ", \"profile\":\"" + profile + '\"' +
                ", \"promoId\":\"" + promoId + '\"' +
                ", \"channel\":\"" + channel + '\"' +
                ", \"registerInfo\":" + registerInfo +
                ", \"otherValue\":\"" + otherValue + '\"' +
                ", \"codePortability\":\"" + codePortability + '\"' +
                ", \"prepaidServices\":" + prepaidServices +
                '}';
    }
}
