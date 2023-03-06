package tests.unit;

import ru.rushydro.cis1c.dkutyrev.dronewithastick.AdvancedEncryptionStandard;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AdvancedEncryptionStandardTest {

    @org.junit.jupiter.api.BeforeAll
    static void setUp(){
        AdvancedEncryptionStandard.setKey(" Sum Summus Mus ");
    }

    @org.junit.jupiter.api.Test
    void encrypt() {

        try {
            byte[] encryptedValue =  AdvancedEncryptionStandard.encrypt("314".getBytes(StandardCharsets.UTF_8));
            String expResult = "Ôß¸³Ã\u0003JH\t\u00117û3\u0087Äk";
            assertEquals(expResult, new String(encryptedValue, "ISO-8859-1"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Encryption exception");
        }

    }

    @org.junit.jupiter.api.Test
    void decrypt() {

        try {
            byte[] decryptedValue =  AdvancedEncryptionStandard.decrypt("Ôß¸³Ã\u0003JH\t\u00117û3\u0087Äk".getBytes("ISO-8859-1"));
            String expResult = "314";
            assertEquals(expResult, new String(decryptedValue));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Encryption exception");
        }


    }
}