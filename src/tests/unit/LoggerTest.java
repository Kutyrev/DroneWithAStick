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

    private Logger logger;

    @BeforeEach
    void setUp() {

        logger = new Logger();
        Notificator.addNotificationObject(logger);
        logger.recieveNotification("Test\n");
    }

    @Test
    void endLogging() {

       ReversedLinesFileReader reversedFileReader = null;
       String expResultOne = "Logging ended";
       String expResultTwo = "Test";
       String lastLine = "";
       String beforeLastLine = "";

        logger.endLogging();

        try {
            reversedFileReader = new ReversedLinesFileReader(new File("dws_log.log"),StandardCharsets.UTF_8);
            lastLine = reversedFileReader.readLine();
            beforeLastLine = reversedFileReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IO Exception");
        }

        assertTrue(lastLine.contains(expResultOne));
        assertTrue(beforeLastLine.contains(expResultTwo));

    }

    @AfterEach
    void tearDown(){
        Notificator.removeNotificationObject(logger);
        logger = null;

    }




    }