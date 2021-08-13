package com.uusafe.platform.test.common.utils;

import com.github.javafaker.*;

import java.util.*;

public class TestValue {
    public static String KEY1 = "hello1";
    public static String KEY2 = "hello2";
    public static String KEY3 = "hello3";
    public static String KEY4 = "hello4";
    public static String KEY5 = "hello5";
    public static String VAL1 = "world1";
    public static String VAL2 = "world2";
    public static String VAL3 = "world3";
    public static String VAL4 = "world4";
    public static String VAL5 = "world5";
    public static String VAL6 = "world6";

    public static Map<String, String> HASH_MAP = new HashMap<>();
    public static Set<String> HASH_SET = new HashSet<>();
    public static List<String> LIST = new LinkedList<>();

    static  {
        HASH_MAP.put(KEY1, VAL1);
        HASH_MAP.put(KEY2, VAL2);
        HASH_MAP.put(KEY3, VAL3);

        HASH_SET.add(KEY4);
        HASH_SET.add(KEY5);
        HASH_SET.add(VAL4);

        LIST.add(VAL5);
        LIST.add(VAL6);
    }

    public static String getParagraph(int maxCharacters) {
        Faker faker = new Faker();
        Lorem lorem = faker.lorem();
        return lorem.characters(1000, maxCharacters);
    }

    public static String getParagraph() {
        return getParagraph(100000);
    }

    public static String getLongLorem() {
        Faker faker = new Faker();
        Lorem lorem = faker.lorem();
        int max = Integer.MAX_VALUE / 1000;
        return lorem.characters(max);
    }

    public static String getRandomFileName() {
        Faker faker = new Faker();
        Lorem lorem = faker.lorem();
        return "file_" + lorem.characters(6, 10);
    }

    public static String getRandomKey() {
        Faker faker = new Faker();
        Name name = faker.name();
        return "key_" + name.name();
    }

    // TODO get your own big file dir here
    public static String getTestBigFileDir() {
        return  "/home/jelly/文档/testbigdata";
//        return "your/path/to/file";
    }

}
