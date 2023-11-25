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
        AtomicInteger check = new AtomicInteger(0);
        boolean proteins = getFPC(Food::getProteins, check);
        boolean fats = getFPC(Food::getFats, check);
        boolean carbohydrates = getFPC(Food::getCarbohydrates, check);
        String balance = " ";

        if (check.get() == 3)
            balance = " уже ";

        if (proteins && fats && carbohydrates) {
            System.out.printf("Корзина%sсбалансирована по БЖУ. ", balance);
        } else
            System.out.println("Невозможно сбалансировать корзину по БЖУ. ");

    }

    private boolean getFPC(Predicate<Food> predicate, AtomicInteger check) {
        if (foodstuffs.stream().noneMatch(predicate))
            return foodstuffs.add((T) market.getThings(Food.class).stream()
                    .filter(predicate)
                    .findAny()
                    .get());
        else {
            check.incrementAndGet();
            return true;
        }
    }

}
