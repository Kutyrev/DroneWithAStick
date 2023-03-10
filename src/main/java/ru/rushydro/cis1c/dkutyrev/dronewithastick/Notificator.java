package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Observer pattern
 */
abstract public class Notificator {

    public static final String DATE_FORMAT_PATTERN = "yyyy.MM.dd hh:mm:ss a zzz";
    public static final String DATE_START_SIGN = "[";
    public static final String DATE_END_SIGN = "] ";
    private static ArrayList<Notification> notificationObjects;

    /**
     * Adds notification observer (listener)
     *
     * @param notificationObject Observer (listener).
     *                           Implements Notification interface.
     */
    public static void addNotificationObject(Notification notificationObject) {

        if (notificationObjects == null) {
            notificationObjects = new ArrayList<Notification>();
        }

        notificationObjects.add(notificationObject);
    }

    /**
     * Removes notification observer (listener)
     *
     * @param notificationObject Observer (listener).
     *                           Implements Notification interface.
     */
    public static void removeNotificationObject(Notification notificationObject) {
        notificationObjects.remove(notificationObject);
    }

    /**
     * Send message to all observers (listeners)
     *
     * @param outputText Message to send
     */
    public static void notifyAllObjects(String outputText) {
        if (notificationObjects == null) {
            return;
        }

        // Инициализация объекта date
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String lastStatus;

        lastStatus = DATE_START_SIGN + formatForDateNow.format(dateNow) + DATE_END_SIGN + outputText + "\n";

        notificationObjects.forEach((curNotifyObject) -> curNotifyObject.recieveNotification(lastStatus));
    }
}
