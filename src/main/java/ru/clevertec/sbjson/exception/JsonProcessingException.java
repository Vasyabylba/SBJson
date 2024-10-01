package ru.clevertec.sbjson.exception;

public class JsonProcessingException extends RuntimeException {
    public JsonProcessingException(String message) {
        super(message);
    }

    public JsonProcessingException(Throwable cause) {
        super(cause);
    }
}
