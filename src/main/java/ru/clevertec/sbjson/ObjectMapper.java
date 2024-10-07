package ru.clevertec.sbjson;

import ru.clevertec.sbjson.core.BaseJsonGenerator;
import ru.clevertec.sbjson.core.BaseJsonParser;
import ru.clevertec.sbjson.core.JsonDeserializer;
import ru.clevertec.sbjson.core.JsonGenerator;
import ru.clevertec.sbjson.core.JsonParser;
import ru.clevertec.sbjson.databind.element.ObjectElement;

import java.util.Map;

public class ObjectMapper {
    protected final JsonGenerator jsonGenerator;
    protected final JsonParser jsonParser;
    protected final JsonDeserializer jsonDeserializer;

    public ObjectMapper() {
        jsonGenerator = new BaseJsonGenerator();
        jsonParser = new BaseJsonParser();
        jsonDeserializer = new JsonDeserializer();
    }

    public String toJson(Object value) {
        ObjectElement objectElement = jsonGenerator.getObjectElement(value);
        return objectElement.serialize();
    }

    public <T> T toObject(String content, Class<T> valueType) throws Exception {
        Map<String, Object> jsonMap = jsonParser.parseJsonObject(content);
        return jsonDeserializer.deserializeObject(jsonMap, valueType);
    }
}
