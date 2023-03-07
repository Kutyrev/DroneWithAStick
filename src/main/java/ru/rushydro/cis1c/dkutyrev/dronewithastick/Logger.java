package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File logger
 */
public class Logger implements Notification {

    public static final String LOG_FILENAME = "dws_log.log";
    public static final String LOGGING_STARTED_MSG = "Logging started";
    public static final String DATE_FORMAT_PATTERN = "yyyy.MM.dd hh:mm:ss a zzz";
    public static final String LOGGING_ENDED_MSG = "Logging ended";
    public static final String DATE_START_SIGN = "[";
    public static final String DATE_END_SIGN = "] ";

    private static FileWriter fw;

    /**
     * Initiates file writer
     */
    public Logger() {
        try {
            fw = new FileWriter(LOG_FILENAME, true);
            Notificator.notifyAllObjects(LOGGING_STARTED_MSG);
            writeToLog(LOGGING_STARTED_MSG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds text to log
     *
     * @param outputText text to log
     */
    private void writeToLog(String outputText) {
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String lastStatus;

        try {
            lastStatus = DATE_START_SIGN + formatForDateNow.format(dateNow) + DATE_END_SIGN + outputText + "\n";
            fw.write(lastStatus);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes file
     */
    public void endLogging() {
        if (fw == null) {
            return;
        }

        try {
            Notificator.notifyAllObjects(LOGGING_ENDED_MSG);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recieveNotification(String outputText) {
        if (fw == null) {
            return;
        }

        try {
            fw.write(outputText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
