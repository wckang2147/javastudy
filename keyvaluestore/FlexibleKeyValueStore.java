package com.lgcns.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class FlexibleKeyValueStore {

    public enum Mode {
        SINGLE,
        LIST
    }

    private final Map<String, Object> store = new HashMap<>();
    private final Mode mode;

    public FlexibleKeyValueStore(String filePath, String keyDelimiter, Mode mode, String valueDelimiter) {
        this.mode = mode;
        loadFromFile(filePath, keyDelimiter, valueDelimiter);
    }

    private void loadFromFile(String filePath, String keyDelimiter, String valueDelimiter) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) continue;

                String[] parts = line.split(keyDelimiter, 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String valueRaw = parts[1].trim();

                    if (mode == Mode.SINGLE) {
                        store.put(key, valueRaw);
                    } else {
                        List<String> valueList = Arrays.asList(valueRaw.split("\\s*" + Pattern.quote(valueDelimiter) + "\\s*"));
                        store.put(key, valueList);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("파일 읽기 오류: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) store.get(key);
    }

    public void printAll() {
        for (Map.Entry<String, Object> entry : store.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }
}
