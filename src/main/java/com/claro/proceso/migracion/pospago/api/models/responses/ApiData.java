package com.claro.proceso.migracion.pospago.api.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApiData {
    private String name;
    private Object data;

    @Override
    public String toString() {
        return String.format("{name:%s, data:%s}",name, data);
    }
}
