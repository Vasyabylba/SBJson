package ru.clevertec.sbjson.databind.element;

import ru.clevertec.sbjson.databind.JsonSerializable;

public abstract class JsonElement implements JsonSerializable {
    protected JsonElement() {
    }

    public abstract JsonElement get(int index);

    public abstract JsonElement get(String fieldName);
}
