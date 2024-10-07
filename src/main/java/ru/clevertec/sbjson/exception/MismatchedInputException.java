package ru.clevertec.sbjson.exception;

public class MismatchedInputException extends RuntimeException {
    public MismatchedInputException(String message) {
        super(message);
    }

    public static MismatchedInputException deserializationTypeFrom(String targetType, String fromType) {
        return new MismatchedInputException(
                String.format(
                        "Cannot deserialize value of type `%s` from %s value",
                        targetType,
                        fromType)
        );
    }
}
