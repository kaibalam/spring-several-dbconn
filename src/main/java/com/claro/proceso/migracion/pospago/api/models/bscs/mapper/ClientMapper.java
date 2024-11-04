package com.claro.proceso.migracion.pospago.api.models.bscs.mapper;


import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ClientDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class ClientMapper implements RowMapper<ClientDataDTO> {
    @Override
    public ClientDataDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        log.info("ResultSet {}", rowNum);
        ClientDataDTO dto = new ClientDataDTO();
        String chStatus = hStatus(rs.getString("chistory_status"));
        String dType = docType(rs.getInt("doc_type"));
        dto.setMsisdn(rs.getString("telefono"));
        dto.setContract(rs.getInt("contrato"));
        dto.setInstallDate(rs.getString("v_fecha"));
        dto.setSecHistory(rs.getInt("sec_history"));
        dto.setCHistoryStatus(chStatus);
        dto.setName(rs.getString("nombre"));
        dto.setLastName(rs.getString("apellido"));
        dto.setDocType(dType);
        dto.setDocNumber(rs.getString("documento_no"));
        dto.setBirthdate(rs.getString("birthdate"));
        dto.setSex(rs.getString("title_sex"));
        dto.setProfileId(rs.getString("profile_id"));
        dto.setCity(rs.getString("city"));
        dto.setAddress(rs.getString("address"));
        dto.setDistrict(rs.getString("district"));
        dto.setNit(rs.getString("nit"));
        dto.setContractExpiration(rs.getString("expiration"));
        dto.setCycle(rs.getString("ciclo"));
        dto.setSimNum(rs.getString("simnum"));
        dto.setBrand(rs.getString("marca"));
        dto.setModel(rs.getString("modelo"));
        dto.setBalance(rs.getDouble("deuda"));
        dto.setBillsCount(rs.getInt("facturas"));
        dto.setClientId(rs.getInt("id_cliente"));
        dto.setDocTypeNumber(rs.getInt("doc_type"));
        dto.setPlan(rs.getInt("plan"));
        return dto;
    }

    public static String hStatus(String status) {
        String chStatus = null;
        switch (status) {
            case "a":
                chStatus = "ACTIVO";
                break;
            case "s":
                chStatus = "SUSPENDIDO";
                break;
            case "d":
                chStatus = "DESACTIVO";
                break;
            default:
                chStatus = "DESCONOCIDO";
        }
        return chStatus;
    }

    public static String docType(Integer type) {
        String docType = null;
        switch (type) {
            case 1:
                docType = "CEDULA";
                break;
            case 2:
                docType = "PASAPORTE";
                break;
            case 3:
                docType = "DUI";
                break;
            case 4:
                docType = "CEDULA";
                break;
            case 5:
                docType = "DNI HN";
                break;
            case 6:
                docType = "DPI";
                break;
            case 7:
                docType = "CDI CR";
                break;
            case 8:
                docType = "CDI CR";
                break;
            case 9:
                docType = "CDI NI";
                break;
            case 10:
                docType = "CDI NI";
                break;
            case 11:
                docType = "DNI HN";
                break;
        }

        return docType;
    }
}
