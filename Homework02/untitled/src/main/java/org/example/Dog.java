package org.example;

public class Dog extends Animal{
    private boolean isHunting;

    public Dog(String name, int age, boolean isHunting) {
        super(name, age);
        this.isHunting = isHunting;
    }


    @Override
    public void makeSound() {
        System.out.printf("%s лает на весь дом, мешая спать соседям", name);
    }

    public void run(){
        System.out.printf("%s осится по квартире, сшибая мебель", name);
    }

    @Override
    public String toString() {
        return "Dog " + name;
    }
}
