package ru.clevertec.sbjson.core;

import ru.clevertec.sbjson.databind.element.ArrayElement;
import ru.clevertec.sbjson.databind.element.JsonElement;
import ru.clevertec.sbjson.databind.element.ObjectElement;

import java.util.Collection;
import java.util.Map;

public interface JsonGenerator {
    ObjectElement getObjectElement(Object value);

    JsonElement getJsonElement(Object value);

    ObjectElement getObjectElement(Map<?, ?> map);

    ArrayElement getArrayElement(Object[] values);

    ArrayElement getArrayElement(Collection<?> collection);
}
