package com.claro.proceso.migracion.pospago.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Servicio de migración de pospago a prepago")
                        .license(new License().name(""))
                        .version("version 1.1.0")
                        .description("Proceso de utilidad para efectuar migraciones de lineas pospago puras e híbridas, inicia \n" +
                                "con la consulta de los datos relativos del cliente, entre los cuales son validaciones mandatorias previo \n" +
                                "a la migración que se cumplan las siguientes: [ Saldo cero, No poseer bloqueos, Estado Activo, \n" +
                                "Contrato vencido, Sin convenio de pago, Valor de prorrateo, Recarga obligatoria prepago], aparte\n" +
                                "de esto se hace la validación que el usuario de asesor posea agencia para habilitarle el proceso de migración. \n" +
                                "El Flujo inicia de la siguiente manera Híbridos: " +
                                "[1] Baja en Pospago, [2] Verificación de la baja exitosa, [3] Baja en prepago, [4] Alta en prepago," +
                                "[5] Envío encabezado a ODA, [6] Envío del Detalle a ODA. Los pospago puro solo cambia el flujo " +
                                "omitiendo el paso 3 de baja en prepago, ejecutando la alta en prepago inmediatamente"));
    }
}
