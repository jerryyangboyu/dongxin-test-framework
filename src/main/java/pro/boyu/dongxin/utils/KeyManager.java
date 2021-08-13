package com.uusafe.platform.test.common.utils;

public class KeyManager {
    private static String key = TestValue.getRandomFileName();

    public static synchronized void setKey(String key) {
        KeyManager.key = key;
    }

    public static synchronized String getKey() {
        return KeyManager.key;
    }

    public static synchronized String refreshKey() {
        key = TestValue.getRandomFileName();
        return key;
    }
}
