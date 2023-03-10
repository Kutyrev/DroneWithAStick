package tests.unit;

import ru.rushydro.cis1c.dkutyrev.dronewithastick.Logger;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Notificator;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class LoggerTest {

    public static final String EXP_RES_1 = "Logging ended";
    public static final String EXP_RES_2 = "Test";
    public static final String TEST_NOTIFICATION = "Test\n";
    public static final String LOG_FILE_NAME = "dws_log.log";
    public static final int LOG_BLOCK_SIZE = 4096;
    public static final String LOG_EXCEPTION = "IO Exception";
    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = new Logger();
        Notificator.addNotificationObject(logger);
        logger.recieveNotification(TEST_NOTIFICATION);
    }

    @Test
    void endLogging() {
        ReversedLinesFileReader reversedFileReader = null;
        String lastLine = "";
        String beforeLastLine = "";

        logger.endLogging();

        try {
            reversedFileReader = new ReversedLinesFileReader(new File(LOG_FILE_NAME), LOG_BLOCK_SIZE, StandardCharsets.UTF_8);
            lastLine = reversedFileReader.readLine();
            beforeLastLine = reversedFileReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            fail(LOG_EXCEPTION);
        }

        assertTrue(lastLine.contains(EXP_RES_1));
        assertTrue(beforeLastLine.contains(EXP_RES_2));
    }

    @AfterEach
    void tearDown() {
        Notificator.removeNotificationObject(logger);
        logger = null;
    }
}