package com.claro.proceso.migracion.pospago.api.models.bscs.dtos;

import com.claro.proceso.migracion.pospago.api.models.crmoe.dtos.AgencyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClientDataDTO {

    private String msisdn;
    private String name;
    private String lastName;
    private Integer clientId;
    private Integer contract;
    private Integer secHistory;
    private String cHistoryStatus;
    private String installDate;
    private Double balance;
    private Integer billsCount;
    private Integer docTypeNumber;
    private String docType;
    private String docNumber;
    private String birthdate;
    private String sex;
    private String profileId;
    private String city;
    private String address;
    private String district;
    private String nit;
    private String imei;
    private String imsi;
    private String cycle;
    private String simNum;
    private String brand;
    private String model;
    private String expirationDate;
    private String agency;
    private Integer orderNumber;
    private String intraSacUrl;
    private String contractExpiration;
    private String postpaidType;
    private Integer plan;
    private AgencyDTO agencyObjectDTO;

}
