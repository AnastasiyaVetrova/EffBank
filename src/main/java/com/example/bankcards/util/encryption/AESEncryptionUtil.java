package com.example.bankcards.util.encryption;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AESEncryptionUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;
    private static final int[] VALID_KEY_LENGTHS = {16, 24, 32};

    @Value("${encryption.aes.keyBase64}")
    private String secretKey;

    private SecretKeySpec secretKeySpec;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        boolean validLength = false;
        for (int len : VALID_KEY_LENGTHS) {
            if (keyBytes.length == len) {
                validLength = true;
                break;
            }
        }

        if (!validLength) {
            throw new IllegalArgumentException("!!!AES key must be 16, 24, or 32 bytes (after Base64 decoding)");
        }
        this.secretKeySpec = new SecretKeySpec(keyBytes, "AES");
    }

    public String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            byte[] iv = generateIV();

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            byte[] encryptedWithIv = new byte[IV_SIZE + encrypted.length];

            System.arraycopy(iv, 0, encryptedWithIv, 0, IV_SIZE);
            System.arraycopy(encrypted, 0, encryptedWithIv, IV_SIZE, encrypted.length);

            return Base64.getEncoder().encodeToString(encryptedWithIv);

        } catch (Exception ex) {
            throw new RuntimeException("Encryption error", ex);
        }
    }

    public String decrypt(String encryptedText) {
        try {
            byte[] encryptedWithIV = Base64.getDecoder().decode(encryptedText);
            byte[] iv = new byte[IV_SIZE];
            byte[] encryptedBytes = new byte[encryptedWithIV.length - IV_SIZE];

            System.arraycopy(encryptedWithIV, 0, iv, 0, IV_SIZE);
            System.arraycopy(encryptedWithIV, IV_SIZE, encryptedBytes, 0, encryptedBytes.length);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

            byte[] decrypted = cipher.doFinal(encryptedBytes);

            return new String(decrypted);

        } catch (Exception ex) {
            throw new RuntimeException("Decryption error", ex);
        }
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
}
