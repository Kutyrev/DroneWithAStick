package tests.integration;

import org.junit.jupiter.api.AfterEach;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.CheckScenario;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.CheckStatus;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Checks;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DEPENDS OF ENVIRONMENT
 */
public class IntegrationTest {

    public static final String PING_TEST_DESC = "ping test";
    public static final String DIRECTORY_TEST_DESC = "directory test";
    public static final String PATH = "\\\\SR-DC-126.corp._____.com\\1C_Exchange\\";
    public static final String WEB_SERVICE_TEST_DESC = "web service test";
    public static final String TEST_WS = "http://sr-dc-320.corp._____.com/TST_002/ws/ws-cons-exch.1cws?wsdl";
    public static final String WS_TEST_USER = "USER";
    public static final String WS_TEST_PASS = "___";
    public static final String WS_ANSWER = "WS_Cons_Exch";
    public static final String COM_TEST_DESC = "com test";
    public static final String COMCONNECTOR = "V82.COMConnector";
    public static final String COM_METHOD = "connect";
    public static final String COM_PATH = "Srvr=sr-dc-320;Ref=TST_002;Usr=WS_Cons_Exch;Pwd=___";
    public static final String PING_SERVER_NAME = "sr-dc-321";

    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
    }

    @org.junit.jupiter.api.BeforeEach
    void beforeEach() {
    }

    @org.junit.jupiter.api.Test
    void pingTest() {
        CheckScenario pingCheck = new CheckScenario(PING_TEST_DESC, CheckScenario.CheckTypes.Ping, 0);
        pingCheck.setServerName(PING_SERVER_NAME);
        CheckStatus pingStatus = Checks.doCheck(pingCheck);
        assertTrue(pingStatus.status);
    }

    @org.junit.jupiter.api.Test
    void directoryTest() {
        CheckScenario dirCheck = new CheckScenario(DIRECTORY_TEST_DESC, CheckScenario.CheckTypes.DirectoryExists, 0);
        dirCheck.setDirPath(PATH);
        CheckStatus dirStatus = Checks.doCheck(dirCheck);
        assertTrue(dirStatus.status);
    }

    @org.junit.jupiter.api.Test
    void webServiceTest() {
        CheckScenario webServiceCheck = new CheckScenario(WEB_SERVICE_TEST_DESC, CheckScenario.CheckTypes.UrlConnection, 0);
        webServiceCheck.setURL(TEST_WS);
        webServiceCheck.setURLLogin(WS_TEST_USER);
        webServiceCheck.setURLPassword(WS_TEST_PASS);
        webServiceCheck.setAnswerInclude(WS_ANSWER);
        CheckStatus webServiceStatus = Checks.doCheck(webServiceCheck);
        assertTrue(webServiceStatus.status);
    }

    @org.junit.jupiter.api.Test
    void comTest() {
        CheckScenario comCheck = new CheckScenario(COM_TEST_DESC, CheckScenario.CheckTypes.ComPlus, 0);
        comCheck.setComClassID(COMCONNECTOR);
        comCheck.setComConnMethod(COM_METHOD);
        comCheck.setComConnParams(COM_PATH);
        CheckStatus comStatus = Checks.doCheck(comCheck);
        assertTrue(comStatus.status);
    }

    @AfterEach
    void tearDown() {
    }
}
