package ru.clevertec.jsonparser.core;

public class JsonElementFactory implements JsonElementCreator {
    public static final JsonElementFactory instance = new JsonElementFactory();

    @Override
    public JsonElement booleanElement(boolean v) {
        return new BooleanElement(v);
    }

    @Override
    public JsonElement nullElement() {
        return NullElement.getInstance();
    }

    @Override
    public JsonElement numberElement(Number value) {
        if (value == null) {
            return nullElement();
        }
        return NumericElement.valueOf(value);
    }

    @Override
    public JsonElement textElement(String text) {
        return TextElement.valueOf(text);
    }

    @Override
    public ArrayElement arrayElement() {
        return new ArrayElement(this);
    }

    @Override
    public ObjectElement objectElement() {
        return new ObjectElement(this);
    }
}
