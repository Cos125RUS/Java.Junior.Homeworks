package org.example;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    }

    private static <T> void info(T obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        String modifier = getModifierName(clazz.getModifiers());
        System.out.println(modifier + "class " + clazz.getSimpleName() + " extends " +
                clazz.getSuperclass().getSimpleName() + " {");
        LinkedList<Field> fields = Arrays.stream(clazz.getSuperclass().getDeclaredFields())
                .collect(Collectors.toCollection(LinkedList::new));
        fields.addAll(Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toCollection(LinkedList::new)));
        for (Field field : fields) {
            field.setAccessible(true);
            modifier = getModifierName(field.getModifiers());
            Object value = field.get(obj);
            if (value.getClass().getSimpleName().equals("String"))
                value = String.format("\"%s\"", value);
            System.out.print("\t" + modifier + field.getType().getSimpleName());
            System.out.println(" " + field.getName() + " = " + value + ";");
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            method.setAccessible(true);
            modifier = getModifierName(method.getModifiers());
            System.out.print("\t" + modifier + method.getReturnType().getSimpleName());
            System.out.println(" " + method.getName() + "() {};");
        }
        System.out.println("}\n");
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