package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import javafx.collections.ObservableList;

import java.nio.charset.StandardCharsets;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Loads and saves test scenario and mail notification settings
 */
public abstract class AppPreferences {

    /**
     * Loads saved test scenario and mail notification settings
     * @param data Test scenario
     * @param mailNotificator Mail notification object
     */
    static public void LoadPreferences(ObservableList<CheckScenario> data, MailNotificator mailNotificator){

        Preferences userPrefs;
        String psswd;

        // Cоздаем объект класса Preferences
        userPrefs = Preferences.userRoot().node("dronewithastick");
        userPrefs = userPrefs.node("scenariotable");

        String[] keys = new String[0];

        try {
            keys = userPrefs.childrenNames();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        for (String key : keys) {

            Preferences scenarioLine = userPrefs.node(key);

            CheckScenario newScenElem = new CheckScenario(scenarioLine.get("checkdesc", ""),
                    CheckScenario.CheckTypes.valueOf(scenarioLine.get("checktype", "urlconnection")),
                    scenarioLine.getInt("checkdelay", 0));

            newScenElem.setServerName(scenarioLine.get("servername", ""));
            newScenElem.setURL(scenarioLine.get("url", ""));
            newScenElem.setURLLogin(scenarioLine.get("login", ""));

            psswd = scenarioLine.get("password", "");

            if (!psswd.equals("")) {

                try {
                    byte[] decryptedCipherText =
                            AdvancedEncryptionStandard.decrypt(psswd.getBytes("ISO-8859-1"));
                    psswd = new String(decryptedCipherText);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            newScenElem.setURLPassword(psswd);
            newScenElem.setAnswerInclude(scenarioLine.get("answerinclude", ""));
            newScenElem.setComClassID(scenarioLine.get("comclassid", ""));
            newScenElem.setComConnMethod(scenarioLine.get("comconnmethod", ""));
            newScenElem.setComConnParams(scenarioLine.get("comconnparams", ""));
            newScenElem.setDirPath(scenarioLine.get("dirpath", ""));

            data.add(newScenElem);
        }

        userPrefs = Preferences.userRoot().node("dronewithastick");
        userPrefs = userPrefs.node("mailnotification");

        mailNotificator.setNotify(userPrefs.getBoolean("notify",false));

        mailNotificator.setMainParams(userPrefs.get("hostname",""),userPrefs.get("port",""),
                userPrefs.get("mailto",""), userPrefs.get("mailfrom",""));

        if(userPrefs.getBoolean("auth", false)){

            psswd = userPrefs.get("password","");

            if(!psswd.equals("")) {

                try {
                    byte[] decryptedCipherText =
                            AdvancedEncryptionStandard.decrypt(psswd.getBytes("ISO-8859-1"));
                    psswd = new String(decryptedCipherText);
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
                  mailNotificator.setAuthParams(userPrefs.get("username",""), psswd,
                    userPrefs.getBoolean("starttls", false));
        }

        mailNotificator.setMessageParams(userPrefs.get("subject",""),
                    userPrefs.get("message",""));
    }

    /**
     * Saves test scenario and mail notification settings
     * @param data Test scenario
     * @param mailNotificator Mail notification object
     */
    static public void SavePreferences(ObservableList<CheckScenario> data, MailNotificator mailNotificator){

        Preferences userPrefs;
        String psswd;

        // Cоздаем объект класса Preferences
        userPrefs = Preferences.userRoot().node("dronewithastick");
        userPrefs = userPrefs.node("scenariotable");

        try {
            userPrefs.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }

        userPrefs = Preferences.userRoot().node("dronewithastick");
        userPrefs = userPrefs.node("scenariotable");

        int i = 0;

        for(CheckScenario curData : data){

            i++;

            Preferences scenarioLine = userPrefs.node(String.valueOf(i));

            scenarioLine.put("checkdesc", curData.getCheckDesc());
            scenarioLine.put("checktype", curData.getEnumCheckType().name());
            scenarioLine.putInt("checkdelay", curData.getCheckDelay());

            scenarioLine.put("servername", curData.getServerName());
            scenarioLine.put("url", curData.getURL());
            scenarioLine.put("login", curData.getURLLogin());

            psswd = curData.getURLPassword();

            if (psswd.equals("")){
                scenarioLine.put("password", "");
            }else {
                byte[] plainText = psswd.getBytes(StandardCharsets.UTF_8);

                try {
                    byte[] cipherText = AdvancedEncryptionStandard.encrypt(plainText);
                    scenarioLine.put("password", new String(cipherText, "ISO-8859-1"));
                } catch (Exception e) {
                    scenarioLine.put("password", "");
                    e.printStackTrace();
                }
            }

            scenarioLine.put("answerinclude", curData.getAnswerInclude());
            scenarioLine.put("comclassid", curData.getComClassID());
            scenarioLine.put("comconnmethod", curData.getComConnMethod());
            scenarioLine.put("comconnparams", curData.getComConnParams());
            scenarioLine.put("dirpath", curData.getDirPath());

        }

        userPrefs = Preferences.userRoot().node("dronewithastick");
        userPrefs = userPrefs.node("mailnotification");

        userPrefs.put("hostname", mailNotificator.getHost().get());
        userPrefs.put("port", mailNotificator.getPort().get());
        userPrefs.put("mailto", mailNotificator.getMailTo().get());
        userPrefs.put("mailfrom", mailNotificator.getMailFrom().get());
        userPrefs.putBoolean("auth", mailNotificator.isAuth().get());

        if(mailNotificator.isAuth().get()) {
            userPrefs.put("username", mailNotificator.getUsername().get());

            psswd = mailNotificator.getPassword().get();

            if (psswd.equals("")){
                userPrefs.put("password", "");
            }else {
                try {
                    byte[] plainText = psswd.getBytes(StandardCharsets.UTF_8);
                    byte[] cipherText = AdvancedEncryptionStandard.encrypt(plainText);
                    userPrefs.put("password", new String(cipherText, "ISO-8859-1"));
                } catch (Exception e) {
                    userPrefs.put("password", "");
                    e.printStackTrace();
                }
            }
            userPrefs.put("starttls", String.valueOf(mailNotificator.isStarttls().get()));
        }

        userPrefs.put("subject", mailNotificator.getSubject().get());
        userPrefs.put("message", mailNotificator.getMessageText().get());
        userPrefs.putBoolean("notify", mailNotificator.isNotify().get());

    }
}
