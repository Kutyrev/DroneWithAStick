package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Encrypts/decrypts passwords
 * Uses AES algorithm
 */
public abstract class AdvancedEncryptionStandard {

    private static byte[] key = " Sum Summus Mus ".getBytes(StandardCharsets.UTF_8);

    private static final String ALGORITHM = "AES";

    /**
     * Sets encryption/decryption key.
     * AES supports key sizes of 16, 24 or 32 bytes.
     * @param key encryption/decryption key
     */
    public static void setKey(String key) {
        AdvancedEncryptionStandard.key = key.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Encrypts the given plain text
     *
     * @param plainText The plain text to encrypt
     */
    public static byte[] encrypt(byte[] plainText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts the given byte array
     *
     * @param cipherText The data to decrypt
     */
    public static byte[] decrypt(byte[] cipherText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(cipherText);
    }

}
