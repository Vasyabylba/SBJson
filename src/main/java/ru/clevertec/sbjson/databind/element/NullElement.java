package ru.clevertec.sbjson.databind.element;

public class NullElement extends ValueElement {
    public static final NullElement instance = new NullElement();

    protected NullElement() {
    }

    public static NullElement getInstance() {
        return instance;
    }

    @Override
    public String serialize() {
        return "null";
    }
}
