package com.claro.proceso.migracion.pospago.api.entities.mactpre;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "TBL_DICCIONARIO_PARAMS")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParamsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "dic_valor")
    private String dicValor;
    @Column(name = "dic_tag")
    private String dicTag;
    @Column(name = "fecha_alta")
    private Date fechaAlta;
    private String activo;
    private String pais;
    private String tipo;
    private String desarrollo;

}
