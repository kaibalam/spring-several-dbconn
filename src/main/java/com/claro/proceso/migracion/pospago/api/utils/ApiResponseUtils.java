package com.claro.proceso.migracion.pospago.api.utils;


import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiStatus;
import com.claro.proceso.migracion.pospago.api.models.enums.ErrorStatus;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiData;
import com.claro.proceso.migracion.pospago.api.models.responses.ApiResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiResponseUtils {

    public static final String ERR_0400 = "ERR_0400";
    public static final String ERR_0404 = "ERR_0404";
    public static final String ERR_0500 = "ERR_0500";
    public static final String ERR_9999 = "ERR_9999";
    public static final String ERR_204 = "ERR_0204";
    public static final String WARN_230 = "WARN_0230";

    private ApiResponseUtils() {
        // should never be initialized
    }

    public static ApiResponse success(ApiResponseType responseType, String name, Object data) {
        return new ApiResponse(ErrorStatus.FALSE, responseType, ApiStatus.SUCCESS, null, null, new ApiData(name, data));
    }

    public static ApiResponse error(ApiResponseType responseType, String errorCode, String errorDescription) {
        return new ApiResponse(ErrorStatus.TRUE, responseType, ApiStatus.ERROR, errorCode, errorDescription, null);
    }

    public static ApiResponse genericServerError() {
        return error(ApiResponseType.GENERIC_FAILURE, ERR_9999, "Generic failure");
    }

    public static ApiResponse badRequest() {
        return error(ApiResponseType.GENERIC_FAILURE, ERR_0400, "Bad Request");
    }

    public static ApiResponse successConfigurations(String name, Object data) {
        return success(ApiResponseType.CONFIGURATION, name, data);
    }

    public static ApiResponse errorConfigurations(String errorCode, String errorDescription) {
        return error(ApiResponseType.CONFIGURATION, errorCode, errorDescription);
    }

    public static boolean numberValidation(String msisdn) {
        boolean valido = true;
        if (!msisdn.matches("[0-9]*")) {
            valido = false;
        }
        return valido;
    }

    public static boolean match(String cadena, String regex){
        if (cadena.length() < 6) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(cadena);
        if (cadenaInvalida(cadena)) return false;
       return matcher.matches();
    }

    public static boolean cadenaInvalida(String cadena) {
        String patron1 = "^0+$";
        if (Pattern.matches(patron1,cadena)){
            return true;
        } else {
            return false;
        }
    }
}
