package ru.clevertec.sbjson.core;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseJsonParser implements JsonParser {
    @Override
    public Map<String, Object> parseJsonObject(String value) {
        value = value.strip().substring(1, value.length() - 1);

        Map<String, Object> jsonMap = new LinkedHashMap<>();
        StringBuilder key = new StringBuilder();
        StringBuilder segment = new StringBuilder();
        boolean inQuotes = false;
        int bracketCount = 0;

        for (char c : value.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
                segment.append(c);
                continue;
            }
            if (inQuotes) {
                segment.append(c);
            } else {
                bracketCount = changeBracketCount(c, bracketCount);
                if (c == ':' && bracketCount == 0) {
                    String stripped = segment.toString().strip();
                    String keyStr = stripped.substring(1, stripped.length() - 1);
                    key.append(keyStr);
                    segment.setLength(0);
                } else if (c == ',' && bracketCount == 0) {
                    jsonMap.put(key.toString(), parseJsonElement(segment.toString().strip()));
                    segment.setLength(0);
                    key.setLength(0);
                } else {
                    segment.append(c);
                }
            }
        }
        if (!key.isEmpty()) {
            jsonMap.put(key.toString(), parseJsonElement(segment.toString().strip()));
        }

        return jsonMap;
    }

    private int changeBracketCount(char c, int bracketCount) {
        if (c == '{' || c == '[') {
            bracketCount++;
        } else if (c == '}' || c == ']') {
            bracketCount--;
        }
        return bracketCount;
    }

    @Override
    public Object parseJsonElement(String value) {
        if (value.equals("null")) {
            return null;
        } else if (value.startsWith("{")) {
            return parseJsonObject(value);
        } else if (value.startsWith("[")) {
            return parseJsonArray(value);
        } else if (value.startsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else {
            return value;
        }
    }

    @Override
    public Object parseJsonArray(String value) {
        value = value.strip();
        value = value.substring(1, value.length() - 1);

        List<Object> list = new ArrayList<>();
        StringBuilder curArrElement = new StringBuilder();
        boolean inQuotes = false;
        int bracketCount = 0;

        for (char c : value.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
                curArrElement.append(c);
                continue;
            }
            if (inQuotes) {
                curArrElement.append(c);
            } else {
                bracketCount = changeBracketCount(c, bracketCount);
                if (c == ',' && bracketCount == 0) {
                    String arrayElement = curArrElement.toString().strip();
                    list.add(parseJsonElement(arrayElement));
                    curArrElement.setLength(0);
                } else {
                    curArrElement.append(c);
                }
            }
        }
        if (!curArrElement.isEmpty()) {
            list.add(parseJsonElement(curArrElement.toString().strip()));
        }
        return list;
    }
}
