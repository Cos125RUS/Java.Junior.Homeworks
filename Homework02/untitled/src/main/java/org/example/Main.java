package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
        Animal[] animals = {new Dog("Пёс", 5, true),
                new Cat("Кот", 3, true)};

        for (Animal animal : animals)
            info(animal);

        System.out.println();
        for (Animal animal : animals)
            use(animal, "makeSound");

        info(new Test());

    }

    private static <T> void info(T obj) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        Class<?> clazz = obj.getClass();
        stringBuilder.append(getModifierName(clazz.getModifiers())).append("class ")
                .append(clazz.getSimpleName());
        if (!clazz.getSuperclass().equals(Object.class))
            stringBuilder.append(" extends ").append(clazz.getSuperclass().getSimpleName());
        stringBuilder.append(" {\n");
        LinkedList<Field> fields = Arrays.stream(clazz.getSuperclass().getDeclaredFields())
                .collect(Collectors.toCollection(LinkedList::new));
        fields.addAll(Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toCollection(LinkedList::new)));
        for (Field field : fields) {
            getFieldString(field, obj, stringBuilder);
        }
        for (Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);
            stringBuilder.append("\t").append(getModifierName(method.getModifiers()))
                    .append(method.getReturnType().getSimpleName())
                    .append(" ").append(method.getName()).append("(");
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length - 1; i++) {
                stringBuilder.append(parameters[i].getType().getSimpleName()).append(" ")
                        .append(parameters[i].getName()).append(", ");
            }
            if (parameters.length > 0)
                stringBuilder.append(parameters[parameters.length - 1].getType().getSimpleName())
                        .append(" ").append(parameters[parameters.length - 1].getName());
            stringBuilder.append(") {}\n");
        }
        stringBuilder.append("}\n");
        System.out.println(stringBuilder.toString());
    }

    private static<T> void getFieldString(Field field, T obj, StringBuilder stringBuilder) throws IllegalAccessException {
        field.setAccessible(true);
        Object value = field.get(obj);
        if (value.getClass().getSimpleName().equals("String"))
            value = String.format("\"%s\"", value);
        stringBuilder.append("\t").append(getModifierName(field.getModifiers()))
                .append(field.getType().getSimpleName()).append(" ").append(field.getName())
                .append(" = ").append(value).append(";\n");
    }

    private static <T> void use(T obj, String methodName) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            if (method.getName().equals(methodName)) {
                method.invoke(obj);
            }
        }
        System.out.println();
    }

    private static String getModifierName(int id) {
        return switch (id) {
            case 1 -> "public ";
            case 2 -> "private ";
            case 4 -> "protected ";
            default -> "";
        };
    }
}