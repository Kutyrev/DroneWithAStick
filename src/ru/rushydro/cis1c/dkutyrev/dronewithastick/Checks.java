package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
//COM +
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.*;

/**
 * Performs checks from the scenario
 */
abstract public class Checks {

    public static final String IO_EXCEPTION_MESSAGE = "IO exception";
    public static final String ARROW_MESSAGE = " -> ";
    public static final String X_SIGN = "X";
    public static final String CHECK_FLAG_SIGN = "✔";
    public static final String URL_IS_EMPTY_MESSAGE = "URL is empty";
    public static final String AUTHORIZATION_PROPERTY = "Authorization";
    public static final String AUTH_TYPE_PROPERTY = "Basic ";
    public static final String WRONG_ANSWER_MESSAGE = "The answer doesn't contain correct text";
    public static final String COLON_SIGN = ":";
    public static final String EMPTY_SERVER_NAME_MESSAGE = "Server name is empty";
    public static final String HOST_NOT_FOUND_MESSAGE = "The remote host isn't reachable in 10000 mc";
    public static final String EMPTY_COM_PARAMS_MESSAGE = "Required comm params are empty";
    public static final String JACOB_LIB_NAME = "\\jacob-1.19-x86.dll";
    public static final String CAN_T_LOAD_JACOB_MESSAGE = "Can't load jacob library";
    public static final String CAN_T_CONNECT_MESSAGE = "Can't connect";
    public static final String DIRECTORY_NOT_FOUND_MESSAGE = "Directory not found";
    public static final String UNKNOWN_CHECK_MESSAGE = "Unknown check";
    static private MailNotificator mailNotificator;

    /**
     * Sets current mail notificator object
     * required for sending status (FAILED/RESTORED) messages
     *
     * @param mailNotificator mail notificator object
     */
    public static void setMailNotificator(MailNotificator mailNotificator) {
        Checks.mailNotificator = mailNotificator;
    }

    /**
     * Main check function
     *
     * @param curTest current test
     * @return CheckStatus true/false and error desc if false
     */
    static public CheckStatus doCheck(CheckScenario curTest) {
        CheckStatus curStatus;

        if (curTest.getEnumCheckType() == CheckScenario.CheckTypes.UrlConnection) {
            try {
                curStatus = Checks.callUrlConnection(curTest.getURL(), curTest.getURLLogin(),
                        curTest.getURLPassword(), curTest.getAnswerInclude());

                changeStatus(curTest, curStatus);

                return curStatus;
            } catch (IOException e) {
                e.printStackTrace();
                curStatus = new CheckStatus(false, curTest.getURL() + ARROW_MESSAGE + IO_EXCEPTION_MESSAGE);
                changeStatus(curTest, curStatus);

                return curStatus;
            }
        } else if (curTest.getEnumCheckType() == CheckScenario.CheckTypes.Ping) {
            try {
                curStatus = Checks.ping(curTest.getServerName());

                changeStatus(curTest, curStatus);

                return curStatus;

            } catch (IOException e) {
                e.printStackTrace();
                curStatus = new CheckStatus(false, curTest.getURL() + ARROW_MESSAGE + IO_EXCEPTION_MESSAGE);
                changeStatus(curTest, curStatus);

                return curStatus;
            }
        } else if (curTest.getEnumCheckType() == CheckScenario.CheckTypes.ComPlus) {
            curStatus = Checks.comConnection(curTest.getComClassID(), curTest.getComConnMethod(),
                    curTest.getComConnParams());

            changeStatus(curTest, curStatus);

            return curStatus;

        } else if (curTest.getEnumCheckType() == CheckScenario.CheckTypes.DirectoryExists) {
            curStatus = Checks.directoryExists(curTest.getDirPath());

            changeStatus(curTest, curStatus);
            return curStatus;
        }

        curStatus = new CheckStatus(false, UNKNOWN_CHECK_MESSAGE);
        changeStatus(curTest, curStatus);

        return curStatus;
    }

    /**
     * Changes status icon and send mail notification
     *
     * @param curTest   current test
     * @param curStatus CheckStatus true/false and error desc if false
     */
    private static void changeStatus(CheckScenario curTest, CheckStatus curStatus) {
        if (!curStatus.status) {
            Notificator.notifyAllObjects(curStatus.errorDesc);
        }

        if (!curTest.getLastBooleanStatus() == curStatus.status && !(mailNotificator == null)) {
            mailNotificator.sendNotification(curTest.checkDescProperty().get(), curStatus.status, curStatus.errorDesc);
        }

        if (!curStatus.status) {
            curTest.setCheckLastStatus(X_SIGN);
        } else {
            curTest.setCheckLastStatus(CHECK_FLAG_SIGN);
        }
    }

    /**
     * Checks the availability of a webservice
     *
     * @param specURL        URL address
     * @param login          Web service basic auth login
     * @param password       Web service basic auth password
     * @param urlTextInclude Text to search on the web service description page that is present in the normal mode
     * @return CheckStatus true/false and error desc if false
     * @throws IOException
     */
    private static CheckStatus callUrlConnection(String specURL, String login, String password,
                                                 String urlTextInclude) throws IOException {
        if (specURL.equals("")) {
            return new CheckStatus(false, URL_IS_EMPTY_MESSAGE);
        }

        URL url = new URL(specURL);

        URLConnection urlConnection = url.openConnection();

        if (!login.equals("")) {
            String userpass = login + COLON_SIGN + password;
            String basicAuth = AUTH_TYPE_PROPERTY + new String(Base64.getEncoder().encode(userpass.getBytes()));
            urlConnection.setRequestProperty(AUTHORIZATION_PROPERTY, basicAuth);
        }

        InputStream in = urlConnection.getInputStream();

        InputStreamReader isr = new InputStreamReader(in);

        int numCharsRead;
        char[] charArray = new char[1024];
        StringBuffer sb = new StringBuffer();
        while ((numCharsRead = isr.read(charArray)) > 0) {
            sb.append(charArray, 0, numCharsRead);
        }
        String result = sb.toString();

        if (result.contains(urlTextInclude)) {
            return new CheckStatus(true);
        } else {
            String logMessage = specURL + ARROW_MESSAGE + WRONG_ANSWER_MESSAGE;
            return new CheckStatus(false, logMessage);
        }

       /* System.out.println("*** BEGIN ***");
        System.out.println(result);
        System.out.println("*** END ***");*/


        // Qualified name of the service:
        //   1st arg is the service URI
        //   2nd is the service name published in the WSDL

        // QName qname = new QName(namespaceURI, localPort);

        // Create, in effect, a factory for the service.

        //Service service = Service.create(url, qname);
    }

    /**
     * Checks the availability of a server
     *
     * @param serverName name or IP address
     * @return CheckStatus true/false and error desc if false
     * @throws IOException
     */
    private static CheckStatus ping(String serverName) throws IOException {
        if (serverName.equals("")) {
            return new CheckStatus(false, EMPTY_SERVER_NAME_MESSAGE);
        }

        InetAddress address = InetAddress.getByName(serverName);
        if (address.isReachable(10000)) {

            return new CheckStatus(true);

        } else {

            String logMessage = serverName + ARROW_MESSAGE + HOST_NOT_FOUND_MESSAGE;
            return new CheckStatus(false, logMessage);

        }
    }

    /**
     * Checks COM connection
     *
     * @param comClassID    Checked object com class, for example "V82.COMConnector"
     * @param comConnMethod Checked object comm connect method, for example "Connect"
     * @param comConnParams Checked object comm connect method params, for example
     *                      "Srvr=sr-dc-320;Ref=TST_DGK_002;Usr=WS_Cons_Exch;Pwd=123"
     * @return CheckStatus true/false and error desc if false
     */
    private static CheckStatus comConnection(String comClassID, String comConnMethod, String comConnParams) {
        if (comClassID.equals("") || comConnMethod.equals("") || comConnParams.equals("")) {
            return new CheckStatus(false, EMPTY_COM_PARAMS_MESSAGE);
        }

        System.setProperty(LibraryLoader.JACOB_DLL_PATH, System.getProperty("user.dir") + JACOB_LIB_NAME);
        try {
            LibraryLoader.loadJacobLibrary();
        } catch (UnsatisfiedLinkError e) {
            String logMessage = comClassID + ARROW_MESSAGE + CAN_T_LOAD_JACOB_MESSAGE;
            return new CheckStatus(false, logMessage);
        }

        ActiveXComponent compProgramID = new ActiveXComponent(comClassID);

        try {
            Variant connected = Dispatch.call(compProgramID, comConnMethod, comConnParams);

        } catch (NullPointerException e) {
            String logMessage = comClassID + ARROW_MESSAGE + CAN_T_CONNECT_MESSAGE;
            return new CheckStatus(false, logMessage);
        } catch (ComFailException e) {
            String logMessage = comClassID + ARROW_MESSAGE + "Can't connect: " + e.getMessage();
            return new CheckStatus(false, logMessage);
        } finally {
            ComThread.Release();
        }

        return new CheckStatus(true);
    }

    /**
     * Checks if directory (shared directory) is available
     *
     * @param path Directory path
     * @return CheckStatus true/false and error desc if false
     */
    private static CheckStatus directoryExists(String path) {
        if (!Files.isDirectory(Paths.get(path))) {
            String logMessage = path + ARROW_MESSAGE + DIRECTORY_NOT_FOUND_MESSAGE;
            return new CheckStatus(false, logMessage);
        }

        return new CheckStatus(true);
    }
}
