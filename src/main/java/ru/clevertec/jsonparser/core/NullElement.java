package ru.clevertec.jsonparser.core;

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
