package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import javafx.collections.ObservableList;

import java.nio.charset.StandardCharsets;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Loads and saves test scenario and mail notification settings
 */
public abstract class AppPreferences {

    public static final java.lang.String PREF_MAIN_NODE = "dronewithastick";
    public static final java.lang.String PREF_TABLE_NODE = "scenariotable";
    public static final String PREF_CHECK_DESK = "checkdesc";
    public static final String PREF_CHECK_TYPE = "checktype";
    public static final String PREF_CHECK_DELAY = "checkdelay";
    public static final String PREF_SERVER_NAME = "servername";
    public static final String PREF_URL = "url";
    public static final String PREF_LOGIN = "login";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_MAIL_NOTIFICATION = "mailnotification";
    public static final String PREF_NOTIFY = "notify";
    public static final String PREF_HOSTNAME = "hostname";
    public static final String PREF_PORT = "port";
    public static final String PREF_MAIL_TO = "mailto";
    public static final String PREF_MAIL_FROM = "mailfrom";
    public static final String PREF_AUTH = "auth";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_START_TLS = "starttls";
    public static final String PREF_SUBJECT = "subject";
    public static final String PREF_MESSAGE = "message";
    public static final String PREF_ANSWER_INCLUDE = "answerinclude";
    public static final String PREF_COMCLASS_ID = "comclassid";
    public static final String PREF_COMCONN_METHOD = "comconnmethod";
    public static final String PREF_COMCONN_PARAMS = "comconnparams";
    public static final String PREF_DIR_PATH = "dirpath";

    /**
     * Loads saved test scenario and mail notification settings
     *
     * @param data            Test scenario
     * @param mailNotificator Mail notification object
     */
    static public void LoadPreferences(ObservableList<CheckScenario> data, MailNotificator mailNotificator) {
        Preferences userPrefs;
        String psswd;

        // Cоздаем объект класса Preferences
        userPrefs = Preferences.userRoot().node(PREF_MAIN_NODE);
        userPrefs = userPrefs.node(PREF_TABLE_NODE);

        String[] keys = new String[0];

        try {
            keys = userPrefs.childrenNames();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        for (String key : keys) {
            Preferences scenarioLine = userPrefs.node(key);

            CheckScenario newScenElem = new CheckScenario(scenarioLine.get(PREF_CHECK_DESK, ""),
                    CheckScenario.CheckTypes.valueOf(scenarioLine.get(PREF_CHECK_TYPE, "urlconnection")),
                    scenarioLine.getInt(PREF_CHECK_DELAY, 0));

            newScenElem.setServerName(scenarioLine.get(PREF_SERVER_NAME, ""));
            newScenElem.setURL(scenarioLine.get(PREF_URL, ""));
            newScenElem.setURLLogin(scenarioLine.get(PREF_LOGIN, ""));

            psswd = scenarioLine.get(PREF_PASSWORD, "");

            if (!psswd.equals("")) {

                try {
                    byte[] decryptedCipherText =
                            AdvancedEncryptionStandard.decrypt(psswd.getBytes(StandardCharsets.ISO_8859_1));
                    psswd = new String(decryptedCipherText);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            newScenElem.setURLPassword(psswd);
            newScenElem.setAnswerInclude(scenarioLine.get(PREF_ANSWER_INCLUDE, ""));
            newScenElem.setComClassID(scenarioLine.get(PREF_COMCLASS_ID, ""));
            newScenElem.setComConnMethod(scenarioLine.get(PREF_COMCONN_METHOD, ""));
            newScenElem.setComConnParams(scenarioLine.get(PREF_COMCONN_PARAMS, ""));
            newScenElem.setDirPath(scenarioLine.get(PREF_DIR_PATH, ""));

            data.add(newScenElem);
        }

        userPrefs = Preferences.userRoot().node(PREF_MAIN_NODE);
        userPrefs = userPrefs.node(PREF_MAIL_NOTIFICATION);

        mailNotificator.setNotify(userPrefs.getBoolean(PREF_NOTIFY, false));

        mailNotificator.setMainParams(userPrefs.get(PREF_HOSTNAME, ""), userPrefs.get(PREF_PORT, ""),
                userPrefs.get(PREF_MAIL_TO, ""), userPrefs.get(PREF_MAIL_FROM, ""));

        if (userPrefs.getBoolean(PREF_AUTH, false)) {
            psswd = userPrefs.get(PREF_PASSWORD, "");

            if (!psswd.equals("")) {

                try {
                    byte[] decryptedCipherText =
                            AdvancedEncryptionStandard.decrypt(psswd.getBytes(StandardCharsets.ISO_8859_1));
                    psswd = new String(decryptedCipherText);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
            mailNotificator.setAuthParams(userPrefs.get(PREF_USERNAME, ""), psswd,
                    userPrefs.getBoolean(PREF_START_TLS, false));
        }

        mailNotificator.setMessageParams(userPrefs.get(PREF_SUBJECT, ""),
                userPrefs.get(PREF_MESSAGE, ""));
    }

    /**
     * Saves test scenario and mail notification settings
     *
     * @param data            Test scenario
     * @param mailNotificator Mail notification object
     */
    static public void SavePreferences(ObservableList<CheckScenario> data, MailNotificator mailNotificator) {
        Preferences userPrefs;
        String psswd;

        // Cоздаем объект класса Preferences
        userPrefs = Preferences.userRoot().node(PREF_MAIN_NODE);
        userPrefs = userPrefs.node(PREF_TABLE_NODE);

        try {
            userPrefs.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        userPrefs = Preferences.userRoot().node(PREF_MAIN_NODE);
        userPrefs = userPrefs.node(PREF_TABLE_NODE);

        int i = 0;

        for (CheckScenario curData : data) {

            i++;

            Preferences scenarioLine = userPrefs.node(String.valueOf(i));

            scenarioLine.put(PREF_CHECK_DESK, curData.getCheckDesc());
            scenarioLine.put(PREF_CHECK_TYPE, curData.getEnumCheckType().name());
            scenarioLine.putInt(PREF_CHECK_DELAY, curData.getCheckDelay());

            scenarioLine.put(PREF_SERVER_NAME, curData.getServerName());
            scenarioLine.put(PREF_URL, curData.getURL());
            scenarioLine.put(PREF_LOGIN, curData.getURLLogin());

            psswd = curData.getURLPassword();

            if (psswd.equals("")) {
                scenarioLine.put(PREF_PASSWORD, "");
            } else {
                byte[] plainText = psswd.getBytes(StandardCharsets.UTF_8);

                try {
                    byte[] cipherText = AdvancedEncryptionStandard.encrypt(plainText);
                    scenarioLine.put(PREF_PASSWORD, new String(cipherText, StandardCharsets.ISO_8859_1));
                } catch (Exception e) {
                    scenarioLine.put(PREF_PASSWORD, "");
                    e.printStackTrace();
                }
            }

            scenarioLine.put(PREF_ANSWER_INCLUDE, curData.getAnswerInclude());
            scenarioLine.put(PREF_COMCLASS_ID, curData.getComClassID());
            scenarioLine.put(PREF_COMCONN_METHOD, curData.getComConnMethod());
            scenarioLine.put(PREF_COMCONN_PARAMS, curData.getComConnParams());
            scenarioLine.put(PREF_DIR_PATH, curData.getDirPath());

        }

        userPrefs = Preferences.userRoot().node(PREF_MAIN_NODE);
        userPrefs = userPrefs.node(PREF_MAIL_NOTIFICATION);

        userPrefs.put(PREF_HOSTNAME, mailNotificator.getHost().get());
        userPrefs.put(PREF_PORT, mailNotificator.getPort().get());
        userPrefs.put(PREF_MAIL_TO, mailNotificator.getMailTo().get());
        userPrefs.put(PREF_MAIL_FROM, mailNotificator.getMailFrom().get());
        userPrefs.putBoolean(PREF_AUTH, mailNotificator.isAuth().get());

        if (mailNotificator.isAuth().get()) {
            userPrefs.put(PREF_USERNAME, mailNotificator.getUsername().get());

            psswd = mailNotificator.getPassword().get();

            if (psswd.equals("")) {
                userPrefs.put(PREF_PASSWORD, "");
            } else {
                try {
                    byte[] plainText = psswd.getBytes(StandardCharsets.UTF_8);
                    byte[] cipherText = AdvancedEncryptionStandard.encrypt(plainText);
                    userPrefs.put(PREF_PASSWORD, new String(cipherText, StandardCharsets.ISO_8859_1));
                } catch (Exception e) {
                    userPrefs.put(PREF_PASSWORD, "");
                    e.printStackTrace();
                }
            }
            userPrefs.put(PREF_START_TLS, String.valueOf(mailNotificator.isStarttls().get()));
        }

        userPrefs.put(PREF_SUBJECT, mailNotificator.getSubject().get());
        userPrefs.put(PREF_MESSAGE, mailNotificator.getMessageText().get());
        userPrefs.putBoolean(PREF_NOTIFY, mailNotificator.isNotify().get());
    }
}
