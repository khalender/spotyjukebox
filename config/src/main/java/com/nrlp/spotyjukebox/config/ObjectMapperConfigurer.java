package com.nrlp.spotyjukebox.config;



import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ObjectMapperConfigurer {
    public static LocalDateTimeSerializer LOCAL_DATETIME_SERIALIZER = new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    public static LocalDateTimeDeserializer LOCAL_DATETIME_DESERIALIZER = new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @Bean
    public ObjectMapper objectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, LOCAL_DATETIME_SERIALIZER);
        module.addDeserializer(LocalDateTime.class, LOCAL_DATETIME_DESERIALIZER);

        return Jackson2ObjectMapperBuilder
                .json()
                .modules(module)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, LOCAL_DATETIME_SERIALIZER);
        module.addDeserializer(LocalDateTime.class, LOCAL_DATETIME_DESERIALIZER);

        return Jackson2ObjectMapperBuilder
                .json()
                .modules(module)
                .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}