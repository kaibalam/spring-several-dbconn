package com.claro.proceso.migracion.pospago.api.wsdl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CustomDeleteResponse {
    private String detailResponse;
    private Integer response;
}

