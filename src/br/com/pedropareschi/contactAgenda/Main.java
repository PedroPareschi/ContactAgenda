package br.com.pedropareschi.contactAgenda;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Main extends Application {

    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.mainStage = primaryStage;
        Parent root=  FXMLLoader.load(getClass().getResource("contactAgenda.fxml"));
        primaryStage.setTitle("Contact Agenda");
        primaryStage.setScene(new Scene(root, 700, 350));
        primaryStage.show();
        primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
    }

    private <T extends Event> void closeWindowEvent(T t) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exiting program");
        alert.setContentText("Are you sure you want to exit? Any non saved action will be deleted permanently");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainStage);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.CANCEL)){
            t.consume();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
