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
public class HeaderODARechRequest {
    private String agencia;
    private Integer codigoCliente;
    private String codigogestor;
    private String codigopais;
    private Integer correlativoventa;
    private Integer cuentadetalle;
    private String direccion;
    private Integer exento;
    private Long fechacreacion;
    private Integer financiamiento;
    private Integer montofinan;
    private String nit;
    private String nombrecliente;
    private Integer numerocuotas;
    private Integer prima;
    private Integer deposito_garantia;
    private Integer anticipado;
    private String registrofiscal;
    private String secuencia;
    private String tipodoc;
    private String tipoventa;
    private Integer totaldesc;
    private Integer totaldescesp;
    private Integer totalorden;
    private String departamento;
    private String municipio;
    private String variablesadi;
    private Integer combo;
    private String gironegocio;
    private String telefono_financia;
    private String contrato_financia;

    @Override
    public String toString() {
        return "{  \"agencia\":\"" + agencia + "\"" +
                ", \"codigoCliente\":" + codigoCliente +
                ", \"codigogestor\":\"  " + codigogestor + "\"" +
                ", \"codigopais\":\"" + codigopais + "\"" +
                ", \"correlativoventa\":" + correlativoventa +
                ", \"cuentadetalle\":" + cuentadetalle +
                ", \"direccion\":\"" + direccion + "\"" +
                ", \"exento\":" + exento +
                ", \"fechacreacion\":" + fechacreacion +
                ", \"financiamiento\":" + financiamiento +
                ", \"montofinan\":" + montofinan +
                ", \"nit\":\"" + nit + "\"" +
                ", \"nombrecliente\":\"" + nombrecliente + "\"" +
                ", \"numerocuotas\":" + numerocuotas +
                ", \"prima\":" + prima +
                ", \"deposito_garantia\":" + deposito_garantia +
                ", \"anticipado\":" + anticipado +
                ", \"registrofiscal\":" + registrofiscal  +
                ", \"secuencia\":" + secuencia +
                ", \"tipodoc\":\"" + tipodoc + "\"" +
                ", \"tipoventa\":\"" + tipoventa + "\"" +
                ", \"totaldesc\":" + totaldesc +
                ", \"totaldescesp\":" + totaldescesp +
                ", \"totalorden\":" + totalorden +
                ", \"departamento\":\"" + departamento + "\"" +
                ", \"municipio\":\"" + municipio + "\"" +
                ", \"variablesadi\":" + variablesadi +
                ", \"combo\":" + combo +
                ", \"gironegocio\":" + gironegocio +
                ", \"telefono_financia\":" + telefono_financia +
                ", \"contrato_financia\":" + contrato_financia +
                '}';
    }
}
