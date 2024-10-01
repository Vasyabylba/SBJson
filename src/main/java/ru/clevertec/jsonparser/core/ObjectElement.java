package ru.clevertec.jsonparser.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ObjectElement extends ContainerElement {
    protected final Map<String, JsonElement> children;

    protected ObjectElement(JsonElementFactory ef) {
        super(ef);
        children = new LinkedHashMap<>();
    }

    @Override
    public JsonElement get(int index) {
        return null;
    }

    @Override
    public JsonElement get(String propertyName) {
        return children.get(propertyName);
    }

    public ObjectElement set(String propertyName, JsonElement value) {
        if (value == null) {
            value = super.nullElement();
        }
        children.put(propertyName, value);
        return this;
    }

    @Override
    public String serialize() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        for (Map.Entry<String, JsonElement> entry : children.entrySet()) {
            String objectItem = generateObjectItem(entry.getKey(), entry.getValue());
            joiner.add(objectItem);
        }
        return joiner.toString();
    }

    private String generateObjectItem(String key, JsonElement value) {
        StringBuilder builder = new StringBuilder();
        writeFieldName(builder, key);
        builder.append(':');
        writeFieldValue(builder, value);
        return builder.toString();
    }

    private void writeFieldName(StringBuilder builder, String key) {
        builder
                .append('\"')
                .append(key)
                .append('\"');
    }

    private void writeFieldValue(StringBuilder builder, JsonElement value) {
        String serializedValue = value.serialize();
        builder.append(serializedValue);
    }
}
