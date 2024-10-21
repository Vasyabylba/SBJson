package ru.clevertec.sbjson.core;

import java.util.Map;

public interface JsonParser {
    Map<String, Object> parseJsonObject(String jsonString);

    Object parseJsonElement(String element);

    Object parseJsonArray(String element);
}
