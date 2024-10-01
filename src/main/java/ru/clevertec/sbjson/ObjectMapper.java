package ru.clevertec.sbjson;

import ru.clevertec.sbjson.core.BaseJsonGenerator;
import ru.clevertec.sbjson.core.JsonGenerator;
import ru.clevertec.sbjson.databind.element.ObjectElement;

public class ObjectMapper {
    protected final JsonGenerator jsonGenerator;

    public ObjectMapper() {
        jsonGenerator = new BaseJsonGenerator();
    }

    public String toJson(Object value) {
        ObjectElement objectElement = jsonGenerator.getObjectElement(value);
        return objectElement.serialize();
    }
}
