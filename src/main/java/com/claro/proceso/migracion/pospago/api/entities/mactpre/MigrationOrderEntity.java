package com.claro.proceso.migracion.pospago.api.entities.mactpre;

import com.claro.proceso.migracion.pospago.api.models.enums.OrderStatus;
import com.claro.proceso.migracion.pospago.api.models.enums.StageProcess;
import com.claro.proceso.migracion.pospago.api.models.enums.StageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "migration_postpre_orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MigrationOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String description;
    private String msisdn;
    private String country;
    @Column(name = "start_date")
    private LocalDateTime migrationStartDate;
    @Column(name = "end_date")
    private LocalDateTime migrationEndDate;
    @Column(name = "request", length = 3000)
    private String requestPayload;
    @Column(name = "response", length = 3000)
    private String responsePayload;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "cantidad_prorrateada")
    private Double amountProration;
    @Column(name = "master_code")
    private String masterCode;
    @Column(name = "status_migration")
    @Enumerated(EnumType.STRING)
    private StageStatus statusMigration;
    @Column(name = "stage_process")
    @Enumerated(EnumType.STRING)
    private StageProcess stageProcess;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private String odaCode;
    private String area;
    private String region;
    private String agency;

    public MigrationOrderEntity(String description, String msisdn, String country, LocalDateTime migrationStartDate, LocalDateTime migrationEndDate,
                                String requestPayload, String responsePayload, String userId, Integer orderId, Double amountProration, String masterCode,
                                StageStatus statusMigration, StageProcess stageProcess, OrderStatus orderStatus, String area, String region, String agency) {
        this.description = description;
        this.msisdn = msisdn;
        this.country = country;
        this.migrationStartDate = migrationStartDate;
        this.migrationEndDate = migrationEndDate;
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.userId = userId;
        this.orderId = orderId;
        this.amountProration = amountProration;
        this.masterCode = masterCode;
        this.statusMigration = statusMigration;
        this.stageProcess = stageProcess;
        this.orderStatus = orderStatus;
        this.area = area;
        this.region = region;
        this.agency = agency;
    }

    public MigrationOrderEntity(String requestPayload, String responsePayload, Integer orderId, StageStatus statusMigration,
                                StageProcess stageProcess, OrderStatus orderStatus) {
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.orderId = orderId;
        this.statusMigration = statusMigration;
        this.stageProcess = stageProcess;
        this.orderStatus = orderStatus;
    }

    public MigrationOrderEntity(String requestPayload, String responsePayload, Integer orderId, StageStatus statusMigration,
                                StageProcess stageProcess, OrderStatus orderStatus, String odaCode) {
        this.requestPayload = requestPayload;
        this.responsePayload = responsePayload;
        this.orderId = orderId;
        this.statusMigration = statusMigration;
        this.stageProcess = stageProcess;
        this.orderStatus = orderStatus;
        this.odaCode = odaCode;
    }

    public MigrationOrderEntity(String description, String msisdn, String country, LocalDateTime migrationStartDate, String userId,
                                Integer orderId, Double amountProration, String masterCode, OrderStatus orderStatus, StageProcess process,
                                StageStatus status, String area, String region, String agency) {
        this.description = description;
        this.msisdn = msisdn;
        this.country = country;
        this.migrationStartDate = migrationStartDate;
        this.userId = userId;
        this.orderId = orderId;
        this.amountProration = amountProration;
        this.masterCode = masterCode;
        this.orderStatus = orderStatus;
        this.stageProcess = process;
        this.statusMigration = status;
        this.area = area;
        this.region = region;
        this.agency = agency;
    }
}
