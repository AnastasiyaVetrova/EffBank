package com.example.bankcards.util.encryption;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class CardNumberEncryptionConverter implements AttributeConverter<String, String> {

    private final AESEncryptionUtil encryptionUtil;

    @Override
    public String convertToDatabaseColumn(String number) {
        return number == null ? null : encryptionUtil.encrypt(number);
    }

    @Override
    public String convertToEntityAttribute(String number) {
        return number == null ? null : encryptionUtil.decrypt(number);
    }
}
