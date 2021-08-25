package pro.boyu.test;

import java.util.Arrays;
import java.util.List;

public class SpecialTest {
    public static void main(String[] args) {
        Arrays.asList(9, 8, 2, 4, 6, 1).stream().sorted().forEach(System.out::println);
    }
}
