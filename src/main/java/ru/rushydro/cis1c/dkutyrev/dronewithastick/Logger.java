package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * File logger
 */
public class Logger implements Notification {

    private static FileWriter fw;

    /**
     * Initiates file writer
     */
    public Logger() {
        try {
            fw = new FileWriter("dws_log.log", true);
            Notificator.notifyAllObjects("Logging started");
            writeToLog("Logging started");
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
        // Инициализация объекта date
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss a zzz");
        String lastStatus;

        try {
            lastStatus = "[" + formatForDateNow.format(dateNow) + "] " + outputText + "\n";
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
            Notificator.notifyAllObjects("Logging ended");
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
