package com.example.bankcards.util.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CardNumberMaskSerializer extends JsonSerializer<String> {

    private static final String MASK = "**** **** **** ";

    @Override
    public void serialize(String cardNumber, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (cardNumber == null || cardNumber.length() <= 4) {
            jsonGenerator.writeString(cardNumber);
            return;
        }
        String mackCard = MASK + cardNumber.substring(cardNumber.length() - 4);

        jsonGenerator.writeString(mackCard);
    }
}
