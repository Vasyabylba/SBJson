package ru.clevertec.jsonparser.core;

public abstract class ContainerElement extends JsonElement implements JsonElementCreator {
    protected final JsonElementFactory elementFactory;

    protected ContainerElement(JsonElementFactory elementFactory) {
        this.elementFactory = elementFactory;
    }

    @Override
    public JsonElement booleanElement(boolean v) {
        return elementFactory.booleanElement(v);
    }

    @Override
    public JsonElement nullElement() {
        return elementFactory.nullElement();
    }

    @Override
    public JsonElement numberElement(Number value) {
        return elementFactory.numberElement(value);
    }

    @Override
    public JsonElement textElement(String text) {
        return elementFactory.textElement(text);
    }

    @Override
    public ArrayElement arrayElement() {
        return elementFactory.arrayElement();
    }

    @Override
    public ObjectElement objectElement() {
        return elementFactory.objectElement();
    }
}
