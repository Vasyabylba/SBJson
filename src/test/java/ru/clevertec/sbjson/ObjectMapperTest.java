package ru.clevertec.sbjson;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import domain.Customer;
import util.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectMapperTest {
    com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;
    ObjectMapper objectMapper;

    public ObjectMapperTest() {
        jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jacksonObjectMapper.registerModule(new JavaTimeModule());
        jacksonObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    void toJson() {
        //given
        Customer customer = TestData.generateCustomer();
        String expected = jacksonObjectMapper.writeValueAsString(customer);

        //when
        String actual = objectMapper.toJson(customer);

        //then
        assertEquals(expected, actual);
    }
}