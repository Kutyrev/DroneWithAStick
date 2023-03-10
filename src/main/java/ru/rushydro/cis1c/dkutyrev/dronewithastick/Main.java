package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author DKutyrev
 */
public class Main extends Application {

    public static final String TITLE = "Drone with a stick";
    public static final String STAGE_IS_CLOSING_MESSAGE = "Stage is closing";
    public static final int WINDOW_WIDTH = 900;
    public static final int WINDOW_HEIGHT = 550;
    public static final String FXML_RES_NAME = "/sample.fxml";
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_RES_NAME));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        System.out.println(STAGE_IS_CLOSING_MESSAGE);

        // Save preferences
        controller.onStageClose();
    }
}
