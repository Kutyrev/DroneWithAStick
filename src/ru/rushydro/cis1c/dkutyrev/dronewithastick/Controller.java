package ru.rushydro.cis1c.dkutyrev.dronewithastick;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

public class Controller implements Initializable, Notification {

    @FXML
    private TableColumn checkDescCol;

    @FXML
    private TableColumn checkTypeCol;

    @FXML
    private TableColumn delayCol, checkStatusCol;

    @FXML
    private TableView<CheckScenario> checkTable = new TableView<>();

    @FXML
    private AnchorPane blankPane;

    //URL connection
    @FXML
    private AnchorPane urlConnectionPane, pingPane, comPane, dirExistsPane;

    @FXML
    private TextField urlAddress, urlLogin, urlPass, serverName, comClassID, comConnMethod, comConnParams;

    @FXML
    private TextArea urlTextInclude, logArea, eMailText;

    @FXML
    private TextField eMailTo, eMailFrom, eMailHost, eMailPort, eMailLogin, eMailPassword, eMailSubject, dirPath;

    @FXML
    private CheckBox eMailNeedAuth, eMailNeedTLS, eMailNotify;

    private final ObservableList<CheckScenario> data =
            FXCollections.observableArrayList();

    private Logger logger;
    private MailNotificator mailNotificator;
    private Timer mainTimer;
    private MainTimerTask mainTimerTask;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Notificator.addNotificationObject(this);

        logger = new Logger();

        Notificator.addNotificationObject(logger);

        mailNotificator = new MailNotificator();
        Checks.setMailNotificator(mailNotificator);

        mainTimer = new Timer();
        mainTimerTask = new MainTimerTask();
        mainTimer.schedule(mainTimerTask, 10000, 1000);

        checkTable.setEditable(true);

        checkDescCol.setCellValueFactory(
                new PropertyValueFactory<>("checkDesc"));

        checkTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("checkType"));

        delayCol.setCellValueFactory(
                new PropertyValueFactory<>("checkDelay"));

        checkStatusCol.setCellValueFactory(
                new PropertyValueFactory<>("checkLastStatus"));

        //checkTable.setEditable(true);

        checkDescCol.setCellFactory(TextFieldTableCell.forTableColumn());

       /* checkTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(CheckScenario.CheckTypes.UrlConnection.name(),
                CheckScenario.CheckTypes.Ping.name(),CheckScenario.CheckTypes.ComPlus.name()));*/

        ObservableList<String> checkTypesNames = FXCollections.observableArrayList();

        for (CheckScenario.CheckTypes curCheckType : CheckScenario.CheckTypes.values()) {
            checkTypesNames.add(curCheckType.CheckTypeName());
        }

        checkTypeCol.setCellFactory(ComboBoxTableCell.forTableColumn(checkTypesNames));

        delayCol.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));

        //checkStatusCol.setCellFactory(TextFieldTableCell.forTableColumn());

        checkStatusCol.setCellFactory(column -> new TableCell<CheckScenario, String>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {

                    setText(item);

                    // Style
                    switch (item) {
                        case "✔":
                            setTextFill(Color.GREEN);
                            break;
                        case "X":
                            setTextFill(Color.RED);
                            break;
                        default:
                            setText(null);
                            setStyle("");
                            break;
                    }
                }
            }
        });

        AppPreferences.LoadPreferences(data, mailNotificator);

        checkTable.setItems(data);

        //<editor-fold desc="Notification settings">
        eMailHost.textProperty().bindBidirectional(mailNotificator.getHost());
        eMailPort.textProperty().bindBidirectional(mailNotificator.getPort());
        eMailTo.textProperty().bindBidirectional(mailNotificator.getMailTo());
        eMailFrom.textProperty().bindBidirectional(mailNotificator.getMailFrom());
        eMailLogin.textProperty().bindBidirectional(mailNotificator.getUsername());
        eMailPassword.textProperty().bindBidirectional(mailNotificator.getPassword());
        eMailSubject.textProperty().bindBidirectional(mailNotificator.getSubject());
        eMailNeedAuth.selectedProperty().bindBidirectional(mailNotificator.isAuth());
        eMailNeedTLS.selectedProperty().bindBidirectional(mailNotificator.isStarttls());
        eMailNotify.selectedProperty().bindBidirectional(mailNotificator.isNotify());
        eMailText.textProperty().bindBidirectional(mailNotificator.getMessageText());
        //</editor-fold desc="Notification settings">


        //<editor-fold desc="URL connection listeners">
        urlAddress.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setURL(newText);
        });

        urlLogin.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setURLLogin(newText);
        });

        urlPass.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setURLPassword(newText);
        });

        urlTextInclude.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setAnswerInclude(newText);
        });
        //</editor-fold desc="URL connection listeners">

        serverName.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setServerName(newText);
        });

        //<editor-fold desc="COM+ connection listeners">
        comClassID.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setComClassID(newText);
        });

        comConnMethod.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setComConnMethod(newText);
        });

        comConnParams.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setComConnParams(newText);
        });

        dirPath.textProperty().addListener((obs, oldText, newText) -> {
            CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
            selectedCells.setDirPath(newText);
        });
        //</editor-fold desc="COM+ connection listeners">

        logArea.setWrapText(true);

        blankPane.toFront();
    }

    public void addButtonAction(ActionEvent actionEvent) {
        data.add(new CheckScenario("", CheckScenario.CheckTypes.UrlConnection, 0));
    }

    public void tableMouseView(MouseEvent mouseEvent) {
        CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();

        if (selectedCells == null) {
            blankPane.toFront();
        } else if (selectedCells.getEnumCheckType() == CheckScenario.CheckTypes.UrlConnection) {
            urlAddress.setText(selectedCells.getURL());
            urlLogin.setText(selectedCells.getURLLogin());
            urlPass.setText(selectedCells.getURLPassword());
            urlTextInclude.setText(selectedCells.getAnswerInclude());
            urlConnectionPane.toFront();
        } else if (selectedCells.getEnumCheckType() == CheckScenario.CheckTypes.Ping) {
            serverName.setText(selectedCells.getServerName());
            pingPane.toFront();
        } else if (selectedCells.getEnumCheckType() == CheckScenario.CheckTypes.ComPlus) {
            comClassID.setText(selectedCells.getComClassID());
            comConnMethod.setText(selectedCells.getComConnMethod());
            comConnParams.setText(selectedCells.getComConnParams());
            comPane.toFront();
        } else if (selectedCells.getEnumCheckType() == CheckScenario.CheckTypes.DirectoryExists) {
            dirPath.setText(selectedCells.getDirPath());
            dirExistsPane.toFront();
        } else {
            blankPane.toFront();
        }
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();
        data.remove(selectedCells);
    }

    public void testCurrent(ActionEvent actionEvent) {
        CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();

        CheckStatus curStatus = Checks.doCheck(selectedCells);
    }

    public void testAll(ActionEvent actionEvent) {
        data.forEach((curTest) -> {
            CheckStatus curStatus = Checks.doCheck(curTest);
        });
    }

    public void onStageClose() {
        logger.endLogging();
        mainTimer.cancel();
        AppPreferences.SavePreferences(data, mailNotificator);
    }

    public void checkTypeChange(TableColumn.CellEditEvent cellEditEvent) {
        CheckScenario selectedCells = checkTable.getSelectionModel().getSelectedItem();

        if (cellEditEvent.getNewValue() == null) {
            blankPane.toFront();
        } else if (cellEditEvent.getNewValue().equals("Url connection")) {
            urlAddress.setPromptText(selectedCells.getURL());
            urlLogin.setPromptText(selectedCells.getURLLogin());
            urlPass.setPromptText(selectedCells.getURLPassword());
            urlTextInclude.setPromptText(selectedCells.getAnswerInclude());
            selectedCells.setEnumCheckType(CheckScenario.CheckTypes.UrlConnection);
            urlConnectionPane.toFront();
        } else if (cellEditEvent.getNewValue().equals("Ping")) {
            serverName.setPromptText(selectedCells.getServerName());
            selectedCells.setEnumCheckType(CheckScenario.CheckTypes.Ping);
            pingPane.toFront();
        } else if (cellEditEvent.getNewValue().equals("Com plus")) {
            comClassID.setPromptText(selectedCells.getComClassID());
            comConnMethod.setPromptText(selectedCells.getComConnMethod());
            comConnParams.setPromptText(selectedCells.getComConnParams());
            selectedCells.setEnumCheckType(CheckScenario.CheckTypes.ComPlus);
            comPane.toFront();
        } else if (cellEditEvent.getNewValue().equals("Directory exists")) {
            selectedCells.setEnumCheckType(CheckScenario.CheckTypes.DirectoryExists);
            dirPath.setPromptText(selectedCells.getDirPath());
            dirExistsPane.toFront();
        } else {
            blankPane.toFront();
        }
    }

    @Override
    public void recieveNotification(String outputText) {
        logArea.appendText(outputText);
    }

    class MainTimerTask extends TimerTask {

        Integer tick = 0;

        @Override
        public void run() {
            tick++;

            data.forEach((curTest) -> {

                if (curTest.getCheckDelay() != 0 && tick % curTest.getCheckDelay() == 0) {
                    CheckStatus curStatus = Checks.doCheck(curTest);
                    if (!curStatus.status) {
                        logArea.appendText(curStatus.errorDesc);
                    }
                }

            });

        }
    }
}
