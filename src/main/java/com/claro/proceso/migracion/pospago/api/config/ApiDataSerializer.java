package com.claro.proceso.migracion.pospago.api.config;

import com.claro.proceso.migracion.pospago.api.models.responses.ApiData;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ApiDataSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeStartObject();
        ApiData apiData = (ApiData) value;
        gen.writeObjectField(apiData.getName(), apiData.getData());
        gen.writeEndObject();

    }
}
