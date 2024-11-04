package com.claro.proceso.migracion.pospago.api.utils;

import com.claro.proceso.migracion.pospago.api.entities.mactpre.MigrationOrderEntity;
import com.claro.proceso.migracion.pospago.api.models.bscs.dtos.ResponseClientDataDTO;
import com.claro.proceso.migracion.pospago.api.models.requests.ActivePrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DetailODARechRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPostPaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropPrepaidRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.DropValidationRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.HeaderODARechRequest;
import com.claro.proceso.migracion.pospago.api.models.requests.PrepaidServicesRequest;
import org.springframework.util.ObjectUtils;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapperUtils {
    private MapperUtils() {

    }

    public static <T, R> void updateFieldIfChanged(T originalValues, T newValues, Function<T, R> fieldGetter,
                                                   Consumer<R> fieldSetter) {
        if (ObjectUtils.isEmpty(fieldGetter.apply(newValues))
                || fieldGetter.apply(originalValues) == fieldGetter.apply(newValues)
                || Objects.equals(fieldGetter.apply(originalValues), fieldGetter.apply(newValues))) {
            return;
        }
        fieldSetter.accept(fieldGetter.apply(newValues));
    }

    public static MigrationOrderEntity buildMigrationEntity(@NotNull MigrationOrderEntity entity) {
        return new MigrationOrderEntity(entity.getId(), entity.getDescription(), entity.getMsisdn(), entity.getCountry(), entity.getMigrationStartDate(),
                entity.getMigrationEndDate(), entity.getRequestPayload(), entity.getResponsePayload(), entity.getUserId(), entity.getOrderId(),
                entity.getAmountProration(), entity.getMasterCode(), entity.getStatusMigration(), entity.getStageProcess(), entity.getOrderStatus(), entity.getOdaCode(),
                entity.getArea(),entity.getRegion(),entity.getAgency());
    }

    public static MigrationOrderEntity buildCreateMigrationEntity(@NotNull MigrationOrderEntity entity, ResponseClientDataDTO req) {
        return new MigrationOrderEntity(entity.getDescription(), req.getData().getMsisdn(), req.getAssessorParams().getCountryCode(),
                LocalDateTime.now(), LocalDateTime.now(), entity.getRequestPayload(), entity.getResponsePayload(), req.getAssessorParams().getUserId(),
                req.getAssessorParams().getOrderId(), req.getAssessorParams().getAmountProration(), req.getAssessorParams().getMasterCode(),
                entity.getStatusMigration(), entity.getStageProcess(), entity.getOrderStatus(), entity.getArea(), entity.getRegion(), entity.getAgency());
    }

    public static DropPostPaidRequest buildDropPost(@NotNull ResponseClientDataDTO res) {
        return new DropPostPaidRequest(res.getAssessorParams().getCountryCode(), res.getData().getContract(), res.getAssessorParams().getAmountProration());
    }

    public static DropValidationRequest buildDropValidation(@NotNull ResponseClientDataDTO res) {
        return new DropValidationRequest(res.getData().getOrderNumber(), res.getAssessorParams().getUserId(), res.getData().getContract(), res.getData().getMsisdn(),
                res.getData().getSimNum());
    }

    public static ActivePrepaidRequest buildRequestActivePrep(@NotNull ResponseClientDataDTO res, PrepaidServicesRequest req) {
        return new ActivePrepaidRequest("string", res.getData().getOrderNumber(), res.getAssessorParams().getMasterCode(), res.getData().getMsisdn(), res.getData().getSimNum(),
                res.getData().getImei(), res.getData().getBrand() != null ? res.getData().getBrand() : "No posee", res.getData().getModel() != null ? res.getData().getModel() : res.getData().getSimNum(),
                "MG-POST-PRE", res.getData().getDocTypeNumber(), res.getData().getDocNumber(), res.getData().getExpirationDate(), res.getData().getName(), res.getData().getLastName(), res.getData().getAddress(),
                String.valueOf(res.getAssessorParams().getRegionCode()), buildDate(res.getData().getBirthdate()), res.getData().getSex(), res.getAssessorParams().getUserId(), "-", res.getData().getProfileId(),
                null, "CRM_Perifericos", 1, "string", "string", req);
    }

    public static DropPrepaidRequest buildRequestDropPrep(@NotNull ResponseClientDataDTO req) {
        return new DropPrepaidRequest(req.getData().getMsisdn(), 1);
    }

    public static HeaderODARechRequest buildRequestODAHeader(@NotNull ResponseClientDataDTO req) {
        return new HeaderODARechRequest(req.getData().getAgency(), req.getData().getClientId(), req.getAssessorParams().getUserId(), req.getAssessorParams().getCountryCode(),
                req.getData().getOrderNumber(), 1, req.getData().getAddress(), 0, System.currentTimeMillis(), 0, 0, req.getData().getNit(),
                req.getData().getName() + " " + req.getData().getLastName(), 0, 0, 0, 0, null, null,
                "FAC", "NR", 0, 0, req.getAssessorParams().getPriceRecharge(), req.getData().getAgencyObjectDTO().getArea(),
                req.getData().getAgencyObjectDTO().getRegion(), null, 0, null, null,null);
    }

    public static DetailODARechRequest buildRequestODADetail(@NotNull ResponseClientDataDTO req) {
        return new DetailODARechRequest(1, null, null, req.getData().getContract(), req.getData().getOrderNumber(),
                "01/01/200", 0, 0, null, null, 0, req.getAssessorParams().getPriceRecharge(),
                null, null, null, "Z05.0002.021", "\"\"", req.getData().getMsisdn(), "N/A", "H000",
                req.getData().getAgency(), 1, 0, 0, 0);
    }

    public static String buildDate(String date) {
        DateFormat originalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat newDate = new SimpleDateFormat("dd/MM/yyyy");
        String formatedDate = "";
        try {
            Date fecha = originalDate.parse(date);
            formatedDate = newDate.format(fecha);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return formatedDate;
    }

}
