package tests.unit;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Notification;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Notificator;

import static org.mockito.Matchers.contains;


public class NotificatorTest {

    public static final String TEST_STRING_ARGUMENT = "Test";
    private Notification notificationMock = null;

    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
    }

    @org.junit.jupiter.api.BeforeEach
    void beforeEach(){
        notificationMock = Mockito.mock(Notification.class);
    }

    @org.junit.jupiter.api.Test
    void addNotificationObject() {
        Notificator.addNotificationObject(notificationMock);
        Notificator.notifyAllObjects(TEST_STRING_ARGUMENT);
        Mockito.verify(notificationMock).recieveNotification(contains(TEST_STRING_ARGUMENT));
    }

    @AfterEach
    void tearDown(){
        Notificator.removeNotificationObject(notificationMock);
    }
}