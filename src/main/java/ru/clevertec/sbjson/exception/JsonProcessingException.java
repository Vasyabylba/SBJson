package ru.clevertec.sbjson.exception;

import java.io.IOException;

public class JsonProcessingException extends IOException {
    public JsonProcessingException(String message) {
        super(message);
    }

    public JsonProcessingException(Throwable cause) {
        super(cause);
    }
}
