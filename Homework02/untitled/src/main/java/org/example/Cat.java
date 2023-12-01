package org.example;

public class Cat extends Animal {
    private boolean useCatTray;

    public Cat(String name, int age, boolean useCatTray) {
        super(name, age);
        this.useCatTray = useCatTray;
    }


    @Override
    public void makeSound() {
        System.out.printf("%s мурчит и трётся об ногу, оставляя шесть на штанах\n", name);
    }

    public void sleep() {
        System.out.printf("%s cпит, свернувшись клубочком на твоей подушке\n", name);
    }
}
