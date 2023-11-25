package org.example.task02;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Корзина
 *
 * @param <T> Еда
 */
public class Cart<T extends Food> {

    /**
     * Товары в магазине
     */
    private final ArrayList<T> foodstuffs;
    private final UMarket market;
    private final Class<T> clazz;

    public Cart(Class<T> clazz, UMarket market) {
        this.clazz = clazz;
        this.market = market;
        foodstuffs = new ArrayList<>();
    }

    public Collection<T> getFoodstuffs() {
        return foodstuffs;
    }

    /**
     * Распечатать список продуктов в корзине
     */
    public void printFoodstuffs() {
        AtomicInteger index = new AtomicInteger(1);
        foodstuffs.forEach(food -> {
            System.out.printf("[%d] %s (Белки: %s Жиры: %s Углеводы: %s)\n",
                    index.getAndIncrement(), food.getName(),
                    food.getProteins() ? "Да" : "Нет",
                    food.getFats() ? "Да" : "Нет",
                    food.getCarbohydrates() ? "Да" : "Нет");
        });
    }

    /**
     * Балансировка корзины
     */
    public void cardBalancing() {
        boolean proteins, fats, carbohydrates;
        int check = 0;
        String balance = " ";

        if (foodstuffs.stream().noneMatch(Food::getProteins))
            proteins = foodstuffs.add((T) getFPC(Food::getProteins));
        else {
            proteins = true;
            check++;
        }

        if (foodstuffs.stream().noneMatch(Food::getFats))
            fats = foodstuffs.add((T) getFPC(Food::getFats));
        else {
            fats = true;
            check++;
        }

        if (foodstuffs.stream().noneMatch(Food::getCarbohydrates))
            carbohydrates = foodstuffs.add((T) getFPC(Food::getCarbohydrates));
        else {
            carbohydrates = true;
            check++;
        }

        if (check == 3)
            balance = " уже ";

        if (proteins && fats && carbohydrates) {
            System.out.printf("Корзина%sсбалансирована по БЖУ. ", balance);
        } else
            System.out.println("Невозможно сбалансировать корзину по БЖУ. ");

    }

    private Food getFPC(Predicate<Food> predicate) {
        return market.getThings(Food.class).stream()
                .filter(predicate)
                .findAny()
                .get();
    }

}
