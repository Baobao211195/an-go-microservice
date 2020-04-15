package com.an.common.converter;

import com.an.common.bean.DriverSchedulePath;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDriverSchedulePathConverter implements Converter<String, DriverSchedulePath> {
    private ObjectMapper objectMapper;

    @Autowired
    public StringToDriverSchedulePathConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public DriverSchedulePath convert(String s) {
        return objectMapper.readValue(s, DriverSchedulePath.class);
    }
}
