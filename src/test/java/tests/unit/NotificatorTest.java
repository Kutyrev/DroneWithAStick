package tests.unit;

import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Notification;
import ru.rushydro.cis1c.dkutyrev.dronewithastick.Notificator;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.contains;


public class NotificatorTest {

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
        Notificator.notifyAllObjects("Test");
       // Mockito.verify(notificationMock).recieveNotification(argThat((someString -> someString.contains("Test")))); //TODO
    }

    @AfterEach
    void tearDown(){
        Notificator.removeNotificationObject(notificationMock);
    }
}