package ru.clevertec.jsonparser.core;

public class TextElement extends ValueElement {
    static final TextElement EMPTY_STRING_NODE = new TextElement("");

    protected final String value;

    public TextElement(String value) {
        this.value = value;
    }

    public static TextElement valueOf(String v) {
        if (v == null) {
            return null;
        }
        if (v.isEmpty()) {
            return EMPTY_STRING_NODE;
        }
        return new TextElement(v);
    }

    @Override
    public String serialize() {
        return '\"' + value + '\"';
    }
}
