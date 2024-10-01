package ru.clevertec.jsonparser;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.jsonparser.domain.Customer;
import util.TestData;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectMapperTest {
    com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        jacksonObjectMapper.registerModule(new JavaTimeModule());
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