package com.claro.proceso.migracion.pospago.api.models.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class DetailODARechRequest {
    private Integer cantidad;
    private String codigodatos;
    private String codigoplan;
    private Integer contrato;
    private Integer correlativoventa;
    private String jerarquia_productos;
    private Integer descuentoespec;
    private Integer descuentoprod;
    private String imei;
    private String nivel;
    private Integer plazo;
    private Integer precioproducto;
    private String sim;
    private String simprice;
    private String sim_sku;
    private String sku;
    private String solicitudbilling;
    private String telefono;
    private String tipodescespe;
    private String almacen;
    private String agencia;
    private Integer codigo_item;
    private Integer prima;
    private Integer monto_finan;
    private Integer combo;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        return sb.append("\"cantidad\":").append(cantidad).append(",")
                .append("\"codigodatos\":").append(codigodatos).append(",")
                .append("\"codigoplan\":").append(codigoplan).append(",")
                .append("\"contrato\":").append(contrato).append(",")
                .append("\"correlativoventa\":").append(correlativoventa).append(",")
                .append("\"jerarquia_productos\":\"").append(jerarquia_productos).append("\",")
                .append("\"descuentoespec\":").append(descuentoespec).append(",")
                .append("\"descuentoprod\":").append(descuentoprod).append(",")
                .append("\"imei\":").append(imei).append(",")
                .append("\"nivel\":").append(nivel).append(",")
                .append("\"plazo\":").append(plazo).append(",")
                .append("\"precioproducto\":").append(precioproducto).append(",")
                .append("\"sim\":").append(sim).append(",")
                .append("\"simprice\":").append(simprice).append(",")
                .append("\"sim_sku\":").append(sim_sku).append(",")
                .append("\"sku\":\"").append(sku).append("\",")
                .append("\"solicitudbilling\":").append(solicitudbilling).append(",")
                .append("\"telefono\":\"").append(telefono).append("\",")
                .append("\"tipodescespe\":\"").append(tipodescespe).append("\",")
                .append("\"almacen\":\"").append(almacen).append("\",")
                .append("\"agencia\":\"").append(agencia).append("\",")
                .append("\"codigo_item\":").append(codigo_item).append(",")
                .append("\"prima\":").append(prima).append(",")
                .append("\"monto_finan\":").append(monto_finan).append(",")
                .append("\"combo\":").append(combo).append("}").toString();
    }
}
