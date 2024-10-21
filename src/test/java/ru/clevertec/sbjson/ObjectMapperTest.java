package ru.clevertec.sbjson;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import domain.Customer;
import ru.clevertec.sbjson.exception.JsonProcessingException;
import ru.clevertec.sbjson.exception.MismatchedInputException;
import util.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ObjectMapperTest {
    com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper;
    ObjectMapper objectMapper;

    public ObjectMapperTest() {
        jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        jacksonObjectMapper.registerModule(new JavaTimeModule());
        jacksonObjectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        jacksonObjectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper = new ObjectMapper();
    }

    @Test
    @SneakyThrows
    void shouldGetJson() {
        //given
        Customer customer = TestData.generateCustomer();
        String expected = jacksonObjectMapper.writeValueAsString(customer);

        //when
        String actual = objectMapper.toJson(customer);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void shouldGetObject() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        //given
        String json = TestData.generateCustomerJson();
        Customer expected = jacksonObjectMapper.readValue(json, Customer.class);

        //when
        Customer actual = objectMapper.toObject(json, Customer.class);

        //then
        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowMismatchedInputException_whenMismatchedInput() {
        //given
        String json = TestData.generateInvalidCustomerJson();

        //when, then
        assertThrows(MismatchedInputException.class, () ->
                objectMapper.toObject(json, Customer.class));
    }
}