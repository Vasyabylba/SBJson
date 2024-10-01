package ru.clevertec.sbjson.databind.element;

public class NumericElement extends ValueElement {
    protected final Number value;

    public NumericElement(Number v) {
        value = v;
    }

    public static NumericElement valueOf(Number v) {
        return new NumericElement(v);
    }

    @Override
    public String serialize() {
        return value.toString();
    }
}
