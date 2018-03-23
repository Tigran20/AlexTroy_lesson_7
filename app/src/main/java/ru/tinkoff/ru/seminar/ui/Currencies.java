package ru.tinkoff.ru.seminar.ui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Currencies {

    private static Set<String> availableCurrencies = new HashSet<>(Arrays.asList(
            "USD",
            "EUR"
    ));

    public static boolean contains(String currency) {
        return availableCurrencies.contains(currency.toUpperCase());
    }

}