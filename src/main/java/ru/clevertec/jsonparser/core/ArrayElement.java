package ru.clevertec.jsonparser.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ArrayElement extends ContainerElement {
    private final List<JsonElement> children;

    protected ArrayElement(JsonElementFactory ef) {
        super(ef);
        children = new ArrayList<>();
    }

    @Override
    public JsonElement get(int index) {
        if ((index >= 0) && (index < children.size())) {
            return children.get(index);
        }
        return null;
    }

    @Override
    public JsonElement get(String fieldName) {
        return null;
    }

    public ArrayElement add(JsonElement value) {
        if (value == null) {
            value = super.nullElement();
        }
        children.add(value);
        return this;
    }

    @Override
    public String serialize() {
        StringJoiner joiner = new StringJoiner(",", "[", "]");
        final List<JsonElement> c = children;
        final int size = c.size();
        for (int i = 0; i < size; ++i) {
            JsonElement value = c.get(i);
            String serializedValue = value.serialize();
            joiner.add(serializedValue);
        }
        return joiner.toString();
    }
}
