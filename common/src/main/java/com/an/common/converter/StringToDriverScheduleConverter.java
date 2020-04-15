package com.an.common.converter;

import com.an.common.bean.DriverSchedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDriverScheduleConverter implements Converter<String, DriverSchedule> {
    private ObjectMapper objectMapper;

    @Autowired
    public StringToDriverScheduleConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    @SneakyThrows
    public DriverSchedule convert(String s) {
        return objectMapper.readValue(s, DriverSchedule.class);
    }
}
