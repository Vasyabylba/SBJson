package ru.clevertec.jsonparser.core;

public abstract class JsonElement implements JsonSerializable {
    protected JsonElement() {
    }

    public abstract JsonElement get(int index);

    public abstract JsonElement get(String fieldName);
}
