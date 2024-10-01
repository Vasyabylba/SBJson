package ru.clevertec.jsonparser;

import ru.clevertec.jsonparser.core.ArrayElement;
import ru.clevertec.jsonparser.core.JsonElement;
import ru.clevertec.jsonparser.core.JsonElementCreator;
import ru.clevertec.jsonparser.core.JsonElementFactory;
import ru.clevertec.jsonparser.core.ObjectElement;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ObjectMapper {
    protected final JsonElementCreator elementCreator;

    public ObjectMapper() {
        elementCreator = JsonElementFactory.instance;
    }

    public String toJson(Object value) {
        ObjectElement objectElement = getObjectElement(value);
        return objectElement.serialize();
    }

    private ObjectElement getObjectElement(Object value) {
        Field[] declaredFields = value.getClass().getDeclaredFields();
        Map<String, Object> valueByField = getValueByFieldMap(value, declaredFields);

        ObjectElement objectElement = elementCreator.objectElement();
        for (Map.Entry<String, Object> entry : valueByField.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            JsonElement jsonElement = getJsonElement(fieldValue);
            objectElement.set(fieldName, jsonElement);
        }
        return objectElement;
    }

    private Map<String, Object> getValueByFieldMap(Object object, Field[] declaredFields) {
        return Arrays.stream(declaredFields)
                .collect(Collectors.toMap(
                        Field::getName,
                        field -> getFieldValue(object, field),
                        (existing, current) -> current,
                        LinkedHashMap::new
                ));
    }

    private Object getFieldValue(Object object, Field field) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonElement getJsonElement(Object value) {
        if (value == null) {
            return elementCreator.nullElement();
        } else if (value instanceof Boolean bool) {
            return elementCreator.booleanElement(bool);
        } else if (value instanceof Number number) {
            return elementCreator.numberElement(number);
        } else if (value instanceof Collection<?> collection) {
            return getArrayElement(collection);
        } else if (value.getClass().isArray()) {
            Object[] values = (Object[]) value;
            return getArrayElement(values);
        } else if (value instanceof Map<?, ?> map) {
            return getObjectElement(map);
        } else if (value instanceof Enum<?> e) {
            return elementCreator.textElement(e.name());
        } else if (value.getClass().getTypeName().startsWith("java")) {
            return elementCreator.textElement(value.toString());
        } else {
            return getObjectElement(value);
        }
    }

    private ObjectElement getObjectElement(Map<?, ?> map) {
        ObjectElement objectElement = elementCreator.objectElement();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            JsonElement jsonElement = getJsonElement(fieldValue);
            objectElement.set(fieldName.toString(), jsonElement);
        }
        return objectElement;
    }

    private ArrayElement getArrayElement(Object[] values) {
        ArrayElement arrayElement = elementCreator.arrayElement();
        for (Object item : values) {
            JsonElement jsonElement = this.getJsonElement(item);
            arrayElement.add(jsonElement);
        }
        return arrayElement;
    }

    private ArrayElement getArrayElement(Collection<?> collection) {
        ArrayElement arrayElement = elementCreator.arrayElement();
        for (Object item : collection) {
            JsonElement jsonElement = this.getJsonElement(item);
            arrayElement.add(jsonElement);
        }
        return arrayElement;
    }
}
