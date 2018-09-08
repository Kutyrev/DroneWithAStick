package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Checks and their properties
 */
public class CheckScenario {

    //<editor-fold desc="Getters/Setters">

    public String getURL() {
        return url.get();
    }
    public SimpleStringProperty serviceSpecURLProperty() {
        return url;
    }
    public void setURL(String newUrl) {this.url.set(newUrl);}

    public String getURLLogin() {
        return login.get();
    }
    public void setURLLogin(String newUrlLogin) {this.login.set(newUrlLogin);}

    public String getURLPassword() {
        return password.get();
    }
    public void setURLPassword(String newUrlPass) {this.password.set(newUrlPass);}

    public String getAnswerInclude() {
        return answerInclude.get();
    }
    public void setAnswerInclude(String newAnswerInclude) {this.answerInclude.set(newAnswerInclude);}

    public String getServerName() {
        return serverName.get();
    }
    public void setServerName(String serverName) {this.serverName.set(serverName);}

    public String getCheckType() {
        return checkType.get();
    }

    public CheckTypes getEnumCheckType() {
        return enumCheckType;
    }

    public SimpleStringProperty checkTypeProperty() {
        return checkType;
    }

    public Integer getCheckDelay() {
        return checkDelay.get();
    }
    public SimpleIntegerProperty checkDelayProperty() {
        return checkDelay;
    }

    public String getCheckDesc() {
        return checkDesc.get();
    }
    public SimpleStringProperty checkDescProperty() {
        return checkDesc;
    }

    public String getComClassID() {
        return comClassID.get();
    }
    public SimpleStringProperty comClassIDProperty() {
        return comClassID;
    }
    public void setComClassID(String comClassID) {
        this.comClassID.set(comClassID);
    }

    public String getComConnMethod() {
        return comConnMethod.get();
    }
    public SimpleStringProperty comConnMethodProperty() {
        return comConnMethod;
    }
    public void setComConnMethod(String comConnMethod) {
        this.comConnMethod.set(comConnMethod);
    }

    public String getComConnParams() {
        return comConnParams.get();
    }
    public SimpleStringProperty comConnParamsProperty() {
        return comConnParams;
    }
    public void setComConnParams(String comConnParams) {
        this.comConnParams.set(comConnParams);
    }


    public String getCheckLastStatus() {
        return checkLastStatus.get();
    }
    public SimpleStringProperty checkLastStatusProperty() {
        return checkLastStatus;
    }
    public void setCheckLastStatus(String checkLastStatus) {
        this.checkLastStatus.set(checkLastStatus);
    }

    public void setDirPath(String dirPath) {
        this.dirPath.set(dirPath);
    }
    public SimpleStringProperty checkDirPathProperty() {
        return dirPath;
    }
    public String getDirPath() {
        return dirPath.get();
    }

    //</editor-fold desc="Getters/Setters">

    public Boolean getLastBooleanStatus() {
        return checkLastStatus.get().equals("✔") || checkLastStatus.get().equals("");
    }

    public enum CheckTypes {
        UrlConnection ("Url connection"),
        Ping ("Ping"),
        ComPlus ("Com plus"),
        DirectoryExists("Directory exists");

        private final String name;

        CheckTypes(String name) {
            this.name = name;
        }
        public String CheckTypeName() { return name; }
    }

    public void setEnumCheckType(CheckTypes enumCheckType) {
        this.enumCheckType = enumCheckType;
        this.checkType =  new SimpleStringProperty(enumCheckType.CheckTypeName());
    }

    //General
    private SimpleStringProperty checkType;
    private CheckTypes enumCheckType;
    private SimpleIntegerProperty checkDelay;
    private SimpleStringProperty checkDesc;
    private SimpleStringProperty checkLastStatus;

    //UrlConnection
    private SimpleStringProperty url;
    private SimpleStringProperty login;
    private SimpleStringProperty password;
    private SimpleStringProperty answerInclude;

    //Ping
    private SimpleStringProperty serverName;

    //Com connection
    private SimpleStringProperty comClassID;
    private SimpleStringProperty comConnMethod;
    private SimpleStringProperty comConnParams;

    //Directory exists
    private SimpleStringProperty dirPath;

    public CheckScenario(String checkDesc, CheckTypes checkType, Integer checkDelay) {
        this.checkDesc = new SimpleStringProperty(checkDesc);
        this.enumCheckType = checkType;
        this.checkType = new SimpleStringProperty(checkType.CheckTypeName());
        this.checkDelay = new SimpleIntegerProperty(checkDelay);
        this.url = new SimpleStringProperty("");
        this.login = new SimpleStringProperty("");
        this.password = new SimpleStringProperty("");
        this.answerInclude = new SimpleStringProperty("");
        this.serverName = new SimpleStringProperty("");
        this.comClassID = new SimpleStringProperty("");
        this.comConnMethod = new SimpleStringProperty("");
        this.comConnParams = new SimpleStringProperty("");
        this.checkLastStatus = new SimpleStringProperty("");
        this.dirPath = new SimpleStringProperty("");
    }

}
