package ru.clevertec.sbjson.databind.element;

public interface JsonElementCreator {
    JsonElement booleanElement(boolean v);

    JsonElement nullElement();

    JsonElement numberElement(Number value);

    JsonElement textElement(String text);

    ArrayElement arrayElement();

    ObjectElement objectElement();
}
