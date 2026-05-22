package com.example.test_student_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    private static final Map<String, String> envVariables = new HashMap<>();

    static {
        loadEnvFile();
    }

    private static void loadEnvFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    envVariables.put(parts[0].trim(), parts[1].trim());
                }
            }
            System.out.println("✅ Environment variables loaded from .env file");
        } catch (IOException e) {
            System.err.println("⚠️ Warning: .env file not found. Using system environment variables.");
        }
    }

    public static String get(String key) {
        // First try from .env file
        String value = envVariables.get(key);
        if (value == null) {
            // Then try system environment variables
            value = System.getenv(key);
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }
}