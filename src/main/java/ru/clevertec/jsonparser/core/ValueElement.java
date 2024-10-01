package ru.clevertec.jsonparser.core;

public abstract class ValueElement extends JsonElement {
    @Override
    public JsonElement get(int index) {
        return null;
    }

    @Override
    public JsonElement get(String fieldName) {
        return null;
    }
}
