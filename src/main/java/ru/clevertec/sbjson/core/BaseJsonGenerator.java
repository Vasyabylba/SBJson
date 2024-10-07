package ru.clevertec.sbjson.core;

import ru.clevertec.sbjson.databind.element.ArrayElement;
import ru.clevertec.sbjson.databind.element.JsonElement;
import ru.clevertec.sbjson.databind.element.JsonElementCreator;
import ru.clevertec.sbjson.databind.element.JsonElementFactory;
import ru.clevertec.sbjson.databind.element.ObjectElement;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseJsonGenerator implements JsonGenerator {
    protected final JsonElementCreator elementCreator;

    public BaseJsonGenerator() {
        this.elementCreator = JsonElementFactory.instance;
    }

    @Override
    public ObjectElement getObjectElement(Object value) {
        Map<String, Object> valueByField = getValueByFieldMap(value);

        ObjectElement objectElement = elementCreator.objectElement();
        for (Map.Entry<String, Object> entry : valueByField.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            JsonElement jsonElement = getJsonElement(fieldValue);
            objectElement.set(fieldName, jsonElement);
        }
        return objectElement;
    }

    @Override
    public JsonElement getJsonElement(Object value) {
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
        } else if (value.getClass().getTypeName().startsWith("java.time")) {
            return elementCreator.textElement(javaTimeAsString(value));
        } else if (value.getClass().getTypeName().startsWith("java")) {
            return elementCreator.textElement(value.toString());
        } else {
            return getObjectElement(value);
        }
    }

    private String javaTimeAsString(Object value) {
        return switch (value) {
            case Instant instant -> DateTimeFormatter.ISO_INSTANT.format(instant);
            case OffsetDateTime offsetDateTime -> DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(offsetDateTime);
            case ZonedDateTime zonedDateTime -> DateTimeFormatter.ISO_ZONED_DATE_TIME.format(zonedDateTime);
            case LocalDateTime localDateTime -> DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(localDateTime);
            case LocalDate localDate -> DateTimeFormatter.ISO_LOCAL_DATE.format(localDate);
            case LocalTime localTime -> DateTimeFormatter.ISO_LOCAL_TIME.format(localTime);
            case OffsetTime offsetTime -> DateTimeFormatter.ISO_OFFSET_TIME.format(offsetTime);
            default -> value.toString();
        };
    }

    @Override
    public ObjectElement getObjectElement(Map<?, ?> map) {
        ObjectElement objectElement = elementCreator.objectElement();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            JsonElement jsonElement = getJsonElement(fieldValue);
            objectElement.set(fieldName.toString(), jsonElement);
        }
        return objectElement;
    }

    @Override
    public ArrayElement getArrayElement(Object[] values) {
        ArrayElement arrayElement = elementCreator.arrayElement();
        for (Object item : values) {
            JsonElement jsonElement = this.getJsonElement(item);
            arrayElement.add(jsonElement);
        }
        return arrayElement;
    }

    @Override
    public ArrayElement getArrayElement(Collection<?> collection) {
        ArrayElement arrayElement = elementCreator.arrayElement();
        for (Object item : collection) {
            JsonElement jsonElement = this.getJsonElement(item);
            arrayElement.add(jsonElement);
        }
        return arrayElement;
    }

    private Map<String, Object> getValueByFieldMap(Object object) {
        Field[] declaredFields = object.getClass().getDeclaredFields();
        return Arrays.stream(declaredFields)
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(
                        LinkedHashMap::new,
                        (map, field) -> map.put(
                                field.getName(),
                                getFieldValue(object, field)
                        ),
                        LinkedHashMap::putAll);
    }

    private Object getFieldValue(Object object, Field field) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
