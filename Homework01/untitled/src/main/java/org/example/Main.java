package org.example;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 1352);
        System.out.println(
                nums.stream()
                        .filter(x -> x % 2 == 0)
                        .mapToDouble(x -> x)
                        .average()
                        .getAsDouble()
        );
    }
}