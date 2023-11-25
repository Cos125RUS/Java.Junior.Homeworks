package org.example.task01;

import java.util.stream.Stream;

public class Average {
    public static void main(String[] args) {
        System.out.println(
                Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
                        .filter(x -> x % 2 == 0)
                        .mapToDouble(x -> x)
                        .average()
                        .getAsDouble()
        );
    }
}