package com.example.bankcards.util.generator;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CardNumberGenerator {

    private static final String PREFIX = "2200";
    private static final int LENGTH = 16;

    private final Random random = new Random();

    public String generate() {
        StringBuilder cardNumber = new StringBuilder(PREFIX);

        while (cardNumber.length() < LENGTH) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }
}
