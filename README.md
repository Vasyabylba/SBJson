# SBJson

SBJson is a Java library that can be used to convert Java Objects into their JSON representation. 
It can also be used to convert a JSON string to an equivalent Java object.

## Using SBJson

The primary class to use is [`ObjectMapper`](src/main/java/ru/clevertec/sbjson/ObjectMapper.java) which you 
can just create by calling `new ObjectMapper()`.

The `ObjectMapper` instance does not maintain any state while invoking JSON operations. So, you are free to reuse 
the same object for multiple JSON serialization and deserialization operations.

## Features

1. `toJson(Object value)` - Serialization of Java objects into JSON strings.

### Parameters

* `value` - Java object for serialization.

### Returns

* Json object as a string.

### Example

```java
ObjectMapper objectMapper = new ObjectMapper();
User user = new User(1 ,"John", "Doe", LocalDate.of(2000, 1, 1));
String jsonString = objectMapper.toJson(customer);
```

2. `toObject(String content, Class<T> valueType)` - Deserialization of JSON strings into Java object.

### Parameters

* `content` - Json object as a string.
* `valueType` - Class of the Java object to be created.

### Returns

* An instance of the specified class, with data from a JSON string.

### Example

```java
ObjectMapper objectMapper = new ObjectMapper();
String jsonUser = """
        {"id":1,"firstName":"John","lastName":"Doe","year":"2000-01-01"}""";
User user = objectMapper.toObject(jsonUser, User.class);
```