package tests.unit;

import ru.rushydro.cis1c.dkutyrev.dronewithastick.AdvancedEncryptionStandard;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AdvancedEncryptionStandardTest {

    public static final String TEST_FAIL_DESC = "Encryption exception";
    public static final String TEST_KEY = " Sum Summus Mus ";
    public static final String TEST_VALUE = "314";
    public static final String TEST_ENCRYPTED_VALUE = "Ôß¸³Ã\u0003JH\t\u00117û3\u0087Äk";

    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
        AdvancedEncryptionStandard.setKey(TEST_KEY);
    }

    @org.junit.jupiter.api.Test
    void encrypt() {
        try {
            byte[] encryptedValue = AdvancedEncryptionStandard.encrypt(TEST_VALUE.getBytes(StandardCharsets.UTF_8));
            assertEquals(TEST_ENCRYPTED_VALUE, new String(encryptedValue, StandardCharsets.ISO_8859_1));
        } catch (Exception e) {
            e.printStackTrace();
            fail(TEST_FAIL_DESC);
        }
    }

    @org.junit.jupiter.api.Test
    void decrypt() {
        try {
            byte[] decryptedValue = AdvancedEncryptionStandard.decrypt(TEST_ENCRYPTED_VALUE.getBytes(StandardCharsets.ISO_8859_1));
            assertEquals(TEST_VALUE, new String(decryptedValue));
        } catch (Exception e) {
            e.printStackTrace();
            fail(TEST_FAIL_DESC);
        }
    }
}