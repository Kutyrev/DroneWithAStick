package ru.rushydro.cis1c.dkutyrev.dronewithastick;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Sends e-mails
 */
public class MailNotificator {

    public static final String RESTORED_CHECK_MSG_PATTERN = " RESTORED check, desc: ";
    public static final String FAILED_CHECK_MSG_PATTERN = " FAILED check, desc: ";
    public static final String MAIL_NOTIFICATION_SENT = "Mail notification sent";
    public static final String MAIL_SMTP_STARTTLS_PROPERTY = "mail.smtp.starttls.enable";
    public static final String MAIL_SMTP_AUTH_PROPERTY = "mail.smtp.auth";
    public static final String MAIL_SMTP_HOST_PROPERTY = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT_PROPERTY = "mail.smtp.port";
    public static final String ERROR_MES_PATTERN = ". Error: ";
    private final SimpleStringProperty host;
    private final SimpleStringProperty port;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty mailTo;
    private final SimpleStringProperty mailFrom;
    private final SimpleStringProperty subject;
    private final SimpleStringProperty messageText;
    private final SimpleBooleanProperty auth;
    private final SimpleBooleanProperty starttls;
    private final SimpleBooleanProperty notify;

    public MailNotificator() {
        host = new SimpleStringProperty();
        port = new SimpleStringProperty();
        username = new SimpleStringProperty();
        password = new SimpleStringProperty();
        mailTo = new SimpleStringProperty();
        mailFrom = new SimpleStringProperty();
        subject = new SimpleStringProperty();
        messageText = new SimpleStringProperty();
        auth = new SimpleBooleanProperty();
        starttls = new SimpleBooleanProperty();
        notify = new SimpleBooleanProperty();

        this.setAuth(false);
        this.setStarttls(false);
        this.setSubject("");
        this.setMessageText("");
        this.setNotify(false);
    }

    /**
     * Sets main mail params
     *
     * @param host     SMTP server
     * @param port     Server port
     * @param mailTo   Destination address
     * @param mailFrom Sender address
     */
    public void setMainParams(String host, String port, String mailTo, String mailFrom) {
        this.setHost(host);
        this.setPort(port);
        this.setMailTo(mailTo);
        this.setMailFrom(mailFrom);
    }

    /**
     * Sets auth mail params
     *
     * @param username Mail account username
     * @param password Mail account password
     * @param starttls Is TLS required
     */
    public void setAuthParams(String username, String password, Boolean starttls) {
        this.setUsername(username);
        this.setPassword(password);
        this.setAuth(true);
        this.setStarttls(starttls);
    }

    /**
     * Sets message subject and text
     *
     * @param subject     Message subject
     * @param messageText Messaige text
     */
    public void setMessageParams(String subject, String messageText) {
        this.setSubject(subject);
        this.setMessageText(messageText);
    }

    /**
     * Sends mail
     *
     * @param checkDesc      Current check description
     * @param checkSuccessed Is last check was successful
     * @param addText        Current check status description
     */
    public void sendNotification(String checkDesc, Boolean checkSuccessed, String addText) {
        String messageText;

        //required fields check
        if (!notify.get() || getHost().getValue() == null || getPort() == null
                || getMailFrom() == null || getMailTo() == null) {
            return;
        }

        Properties props = new Properties();
        props.put(MAIL_SMTP_STARTTLS_PROPERTY, String.valueOf(isStarttls().get()));
        props.put(MAIL_SMTP_AUTH_PROPERTY, String.valueOf(isAuth().get()));
        props.put(MAIL_SMTP_HOST_PROPERTY, getHost().get());
        props.put(MAIL_SMTP_PORT_PROPERTY, getPort().get());

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(getUsername().get(), getPassword().get());
                    }
                });

        if (checkSuccessed) {
            messageText = getMessageText().get() + RESTORED_CHECK_MSG_PATTERN + checkDesc;
        } else {
            messageText = getMessageText().get() + FAILED_CHECK_MSG_PATTERN + checkDesc + ERROR_MES_PATTERN + addText;
        }

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(getMailFrom().get()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(getMailTo().get()));
            message.setSubject(getSubject().get());
            message.setText(messageText);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        Notificator.notifyAllObjects(MAIL_NOTIFICATION_SENT);
    }

    //<editor-fold desc="Getters/Setters">

    public SimpleStringProperty getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }

    public SimpleStringProperty getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port.set(port);
    }

    public SimpleStringProperty getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public SimpleStringProperty getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public SimpleStringProperty getMailTo() {
        return mailTo;
    }

    public void setMailTo(String mailTo) {
        this.mailTo.set(mailTo);
    }

    public SimpleStringProperty getMailFrom() {
        return mailFrom;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom.set(mailFrom);
    }

    public SimpleStringProperty getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public SimpleStringProperty getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText.set(messageText);
    }

    public BooleanProperty isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth.set(auth);
    }

    public BooleanProperty isStarttls() {
        return starttls;
    }

    public void setStarttls(boolean starttls) {
        this.starttls.set(starttls);
    }

    public BooleanProperty isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify.set(notify);
    }

    //</editor-fold desc="Getters/Setters">
}
