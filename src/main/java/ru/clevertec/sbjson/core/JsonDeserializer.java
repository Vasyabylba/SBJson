package ru.clevertec.sbjson.core;

import ru.clevertec.sbjson.exception.JsonProcessingException;
import ru.clevertec.sbjson.exception.MismatchedInputException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.stream.Collectors;

public class JsonDeserializer {
    public <T> T deserializeObject(Map<String, Object> value, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();

        for (Map.Entry<String, Object> field : value.entrySet()) {
            String fieldName = field.getKey();
            Object fieldValue = field.getValue();

            checkIfClassContainsFieldWithName(clazz, fieldName);

            Field clazzField = clazz.getDeclaredField(fieldName);
            clazzField.setAccessible(true);
            Class<?> fieldType = clazzField.getType();
            Type fieldGenericType = clazzField.getGenericType();

            clazzField.set(instance, deserializeElement(fieldValue, fieldType, fieldGenericType));
        }

        return instance;
    }

    public <T> T deserializeElement(Object value, Class<T> clazz, Type type) throws Exception {
        if (value == null) {
            return null;
        } else if (isBoolean(clazz)) {
            return deserializeBoolean(value, type);
        } else if (isNumber(clazz)) {
            return deserializeNumber(value, clazz, type);
        } else if (isDateTime(clazz)) {
            return deserializeDateTime(value, clazz, type);
        } else if (clazz.isEnum()) {
            return deserializeEnum(value, clazz, type);
        } else if (clazz.isArray()) {
            return deserializeArray(value, clazz, type);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            return deserializeCollection(value, clazz, type);
        } else if (Map.class.isAssignableFrom(clazz)) {
            return deserializeMap(value, clazz, type);
        } else if (value instanceof Map) {
            return deserializeObject((Map<String, Object>) value, clazz);
        } else if (value instanceof String strValue) {
            return deserializeString(strValue, clazz, type);
        } else {
            throw new JsonProcessingException(String.format("Unsupported field type: %s", clazz.getTypeName()));
        }
    }

    public <T> T deserializeMap(Object value, Class<T> clazz, Type type) throws Exception {
        if (!(value instanceof Map<?, ?> srcMap)) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Object");
        }

        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        Type keyType = typeArguments[0];
        Type valueType = typeArguments[1];
        T targetMap = getMapImpl(clazz);

        for (Map.Entry<?, ?> entry : srcMap.entrySet()) {
            Method methodAdd = clazz.getDeclaredMethod("put", Object.class, Object.class);
            methodAdd.invoke(
                    targetMap,
                    deserializeElement(entry.getKey(), (Class<?>) keyType, keyType),
                    deserializeElement(entry.getValue(), (Class<?>) valueType, valueType)
            );
        }

        return targetMap;
    }

    @SuppressWarnings("unchecked")
    private <T> T getMapImpl(Class<T> clazz) {
        if (clazz == SortedMap.class || clazz == TreeMap.class) {
            return (T) new TreeMap<>();
        } else if (clazz == HashMap.class) {
            return (T) new HashMap<>();
        } else if (clazz == Hashtable.class) {
            return (T) new Hashtable<>();
        } else return (T) new LinkedHashMap<>();
    }

    public <T> T deserializeCollection(Object value, Class<T> clazz, Type type) throws Exception {
        if (!(value instanceof List<?> srcList)) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Array");
        }
        if (List.class.isAssignableFrom(clazz)) {
            return deserializeList(srcList, clazz, type);
        } else if (Queue.class.isAssignableFrom(clazz)) {
            return deserializeQueue(srcList, clazz, type);
        } else {
            return deserializeSet(srcList, clazz, type);
        }
    }

    private <T> T deserializeSet(List<?> value, Class<T> clazz, Type type) throws Exception {
        T set = getSetImpl(clazz);
        fillCollection(value, clazz, (ParameterizedType) type, set);
        return set;
    }

    @SuppressWarnings("unchecked")
    private <T> T getSetImpl(Class<T> clazz) {
        if (clazz == SortedSet.class || clazz == TreeSet.class) {
            return (T) new TreeSet<>();
        } else if (clazz == LinkedHashSet.class) {
            return (T) new LinkedHashSet<>();
        } else return (T) new HashSet<>();
    }

    private <T> T deserializeQueue(List<?> value, Class<T> clazz, Type type) throws Exception {
        T queue = getQueueImpl(clazz);
        fillCollection(value, clazz, (ParameterizedType) type, queue);
        return queue;
    }

    @SuppressWarnings("unchecked")
    private <T> T getQueueImpl(Class<T> clazz) {
        if (clazz == ArrayDeque.class) {
            return (T) new ArrayDeque<>();
        } else if (clazz == LinkedList.class) {
            return (T) new LinkedList<>();
        } else if (clazz == PriorityQueue.class) {
            return (T) new PriorityQueue<>();
        } else return (T) new LinkedList<>();
    }

    private <T> T deserializeList(List<?> value, Class<T> clazz, Type type) throws Exception {
        T list = getListImpl(clazz);
        fillCollection(value, clazz, (ParameterizedType) type, list);
        return list;
    }

    private <T> void fillCollection(List<?> value, Class<T> clazz, ParameterizedType parameterizedType, T collection) throws Exception {
        Type listTypeArgument = parameterizedType.getActualTypeArguments()[0];
        for (Object element : value) {
            Method methodAdd = clazz.getDeclaredMethod("add", Object.class);
            methodAdd.invoke(collection, deserializeElement(
                    element,
                    (Class<?>) listTypeArgument,
                    listTypeArgument)
            );
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T getListImpl(Class<T> clazz) {
        if (clazz == Stack.class) {
            return (T) new Stack<>();
        } else if (clazz == Vector.class) {
            return (T) new Vector<>();
        } else if (clazz == LinkedList.class) {
            return (T) new LinkedList<>();
        } else return (T) new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public <T> T deserializeArray(Object value, Class<T> clazz, Type type) throws Exception {
        if (!(value instanceof List<?> srcList)) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Array");
        }
        Class<?> componentType = clazz.getComponentType();
        return (T) getFilledArray(srcList, componentType);
    }

    private <T> T[] getFilledArray(List<?> value, Class<T> clazz) throws Exception {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(clazz, value.size());
        for (int i = 0; i < value.size(); i++) {
            array[i] = deserializeElement(value.get(i), clazz, clazz);
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    private <T> T deserializeString(String value, Class<T> clazz, Type type) {
        if (clazz == UUID.class) {
            try {
                return (T) UUID.fromString(value);
            } catch (IllegalArgumentException e) {
                throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "String");
            }
        } else {
            return (T) String.valueOf(value);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T deserializeEnum(Object value, Class<T> clazz, Type type) {
        if (!(value instanceof String valueAsString)) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "String");
        }
        return (T) Enum.valueOf((Class<Enum>) clazz, valueAsString);
    }

    @SuppressWarnings("unchecked")
    public <T> T deserializeDateTime(Object value, Class<T> clazz, Type type) {
        try {
            if (!(value instanceof String valueAsString)) {
                throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "String");
            }
            if (clazz == Instant.class) {
                return (T) Instant.parse(valueAsString);
            } else if (clazz == OffsetDateTime.class) {
                return (T) OffsetDateTime.parse(valueAsString);
            } else if (clazz == ZonedDateTime.class) {
                return (T) ZonedDateTime.parse(valueAsString);
            } else if (clazz == Duration.class) {
                return (T) Duration.parse(valueAsString);
            } else if (clazz == LocalDateTime.class) {
                return (T) LocalDateTime.parse(valueAsString);
            } else if (clazz == LocalDate.class) {
                return (T) LocalDate.parse(valueAsString);
            } else if (clazz == LocalTime.class) {
                return (T) LocalTime.parse(valueAsString);
            } else if (clazz == MonthDay.class) {
                return (T) MonthDay.parse(valueAsString);
            } else if (clazz == OffsetTime.class) {
                return (T) OffsetTime.parse(valueAsString);
            } else if (clazz == Period.class) {
                return (T) Period.parse(valueAsString);
            } else if (clazz == Year.class) {
                return (T) Year.parse(valueAsString);
            } else if (clazz == YearMonth.class) {
                return (T) YearMonth.parse(valueAsString);
            } else {
                throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "String");
            }
        } catch (DateTimeParseException e) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "String");
        }
    }

    private boolean isDateTime(Class<?> clazz) {
        return clazz == Instant.class || clazz == OffsetDateTime.class ||
               clazz == ZonedDateTime.class || clazz == Duration.class ||
               clazz == LocalDateTime.class || clazz == LocalDate.class ||
               clazz == LocalTime.class || clazz == MonthDay.class ||
               clazz == OffsetTime.class || clazz == Period.class ||
               clazz == Year.class || clazz == YearMonth.class;
    }

    private boolean isNumber(Class<?> clazz) {
        return clazz == Byte.class || clazz == Byte.TYPE ||
               clazz == Short.class || clazz == Short.TYPE ||
               clazz == Integer.class || clazz == Integer.TYPE ||
               clazz == Long.class || clazz == Long.TYPE ||
               clazz == Float.class || clazz == Float.TYPE ||
               clazz == Double.class || clazz == Double.TYPE ||
               clazz == BigDecimal.class || clazz == BigInteger.class;
    }

    @SuppressWarnings("unchecked")
    public <T> T deserializeNumber(Object value, Class<T> clazz, Type type) {
        try {
            if (!(value instanceof String valueAsString)) {
                throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Number");
            } else if (clazz == Byte.class || clazz == Byte.TYPE) {
                return (T) Byte.valueOf(valueAsString);
            } else if (clazz == Short.class || clazz == Short.TYPE) {
                return (T) Short.valueOf(valueAsString);
            } else if (clazz == Integer.class || clazz == Integer.TYPE) {
                return (T) Integer.valueOf(valueAsString);
            } else if (clazz == Long.class || clazz == Long.TYPE) {
                return (T) Long.valueOf(valueAsString);
            } else if (clazz == Float.class || clazz == Float.TYPE) {
                return (T) Float.valueOf(valueAsString);
            } else if (clazz == Double.class || clazz == Double.TYPE) {
                return (T) Double.valueOf(valueAsString);
            } else if (clazz == BigDecimal.class) {
                return (T) new BigDecimal(valueAsString);
            } else if (clazz == BigInteger.class) {
                return (T) new BigInteger(valueAsString);
            } else {
                throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Number");
            }
        } catch (NumberFormatException e) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Number");
        }
    }

    private boolean isBoolean(Class<?> clazz) {
        return clazz == Boolean.class || clazz == Boolean.TYPE;
    }

    @SuppressWarnings("unchecked")
    public <T> T deserializeBoolean(Object value, Type type) {
        if (!(value instanceof String valueAsString)) {
            throw MismatchedInputException.deserializationTypeFrom(type.getTypeName(), "Boolean");
        }
        return (T) Boolean.valueOf(valueAsString);
    }

    private void checkIfClassContainsFieldWithName(Class<?> clazz, String fieldName) {
        Set<String> clazzFieldNames = getClassFieldNames(clazz);
        if (!clazzFieldNames.contains(fieldName)) {
            throw new JsonProcessingException(String.format(
                    "Unrecognized field \"%s\" (class %s)",
                    fieldName,
                    clazz.getName()
            ));
        }
    }

    private Set<String> getClassFieldNames(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .map(Field::getName)
                .collect(Collectors.toSet());
    }
}
