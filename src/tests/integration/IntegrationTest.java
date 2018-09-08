package tests.integration;

import ru.rushydro.cis1c.dkutyrev.dronewithastick.*;
import org.junit.jupiter.api.AfterEach;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.CheckScenario;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.CheckStatus;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Checks;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * DEPENDS OF ENVIRONMENT
 */
public class IntegrationTest {

    @org.junit.jupiter.api.BeforeAll
        static void setUp() {


        }

        @org.junit.jupiter.api.BeforeEach
        void beforeEach(){

        }

        @org.junit.jupiter.api.Test
        void pingTest() {

            CheckScenario pingCheck = new CheckScenario("ping test", CheckScenario.CheckTypes.Ping, 0);
            pingCheck.setServerName("sr-dc-321");
            CheckStatus pingStatus = Checks.doCheck(pingCheck);
            assertEquals(true, pingStatus.status);


        }

        @org.junit.jupiter.api.Test
        void directoryTest() {

            CheckScenario dirCheck = new CheckScenario("directory test", CheckScenario.CheckTypes.DirectoryExists, 0);
            dirCheck.setDirPath("\\\\SR-DC-126.corp.gidroogk.com\\1C_Exchange\\");
            CheckStatus dirStatus = Checks.doCheck(dirCheck);
            assertEquals(true, dirStatus.status);
        }

        @org.junit.jupiter.api.Test
        void webServiceTest() {

            CheckScenario webServiceCheck = new CheckScenario("web service test", CheckScenario.CheckTypes.UrlConnection, 0);
            webServiceCheck.setURL("http://sr-dc-320.corp.gidroogk.com/TST_DGK_002/ws/ws-cons-exch.1cws?wsdl");
            webServiceCheck.setURLLogin("USER");
            webServiceCheck.setURLPassword("314");
            webServiceCheck.setAnswerInclude("WS_Cons_Exch");
            CheckStatus webServiceStatus = Checks.doCheck(webServiceCheck);
            assertEquals(true, webServiceStatus.status);
        }

        @org.junit.jupiter.api.Test
        void comTest() {

            CheckScenario comCheck = new CheckScenario("com test", CheckScenario.CheckTypes.ComPlus, 0);
            comCheck.setComClassID("V82.COMConnector");
            comCheck.setComConnMethod("connect");
            comCheck.setComConnParams("Srvr=sr-dc-320;Ref=TST_DGK_002;Usr=WS_Cons_Exch;Pwd=123");
            CheckStatus comStatus = Checks.doCheck(comCheck);
            assertEquals(true, comStatus.status);
        }

        @AfterEach
        void tearDown(){

        }



}
