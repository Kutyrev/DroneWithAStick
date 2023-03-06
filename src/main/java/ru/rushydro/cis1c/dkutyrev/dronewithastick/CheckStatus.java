package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Object-result of a check
 * true/false and error desc if false
 */
public class CheckStatus {

    public static final String DATE_PATTERN = "yyyy.MM.dd hh:mm:ss a zzz";
    public boolean status;
    public String errorDesc;

    public CheckStatus(boolean status) {
        this.status = status;
    }

    public CheckStatus(boolean status, String errorDesc) {
        // Инициализация объекта date
        Date dateNow = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat(DATE_PATTERN);

        this.status = status;
        this.errorDesc = "[" + formatForDateNow.format(dateNow) + "] " + errorDesc + "\n";
    }
}
