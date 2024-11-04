package com.claro.proceso.migracion.pospago.api.models.responses;

import com.claro.proceso.migracion.pospago.api.config.ApiDataSerializer;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiResponseType;
import com.claro.proceso.migracion.pospago.api.models.enums.ApiStatus;
import com.claro.proceso.migracion.pospago.api.models.enums.ErrorStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private ErrorStatus hasError;
    private ApiResponseType responseType;
    private ApiStatus status;
    private String errorCode;
    private String errorDescription;
    private ApiData data;


    public ApiResponseType getResponseType() {
        return responseType;
    }

    public ApiResponse setResposeType(ApiResponseType responseType) {
        this.responseType = responseType;
        return this;
    }

    public ApiStatus getStatus() {
        return status;
    }

    public ApiResponse setStatus(ApiStatus status) {
        this.status = status;
        return this;
    }

    public ErrorStatus getHasError() {
        return hasError;
    }

    public ApiResponse setHasError(ErrorStatus hasError) {
        this.hasError = hasError;
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getErrorCode() {
        return errorCode;
    }

    public ApiResponse setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getErrorDescription() {
        return errorDescription;
    }

    public ApiResponse setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ApiDataSerializer.class)
    public ApiData getData() {
        return data;
    }

    public ApiResponse setData(ApiData data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return String.format("{hasError:%s,responseType:%s, status:%s, errorCode:%s, errorDescription=%s, data:%s},",
                hasError, responseType, status, errorCode, errorDescription, data);
    }
}
