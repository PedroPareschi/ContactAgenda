package br.com.pedropareschi.contactAgenda;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TableView<Contact> contactListView;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContactData contactData;
    @FXML
    private TableColumn<Contact, String> firstNameCol;
    @FXML
    private TableColumn<Contact, String> lastNameCol;
    @FXML
    private TableColumn<Contact, String> phoneNumberCol;
    @FXML
    private TableColumn<Contact, String> notesCol;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        contactData = new ContactData();
        contactData.loadContacts();
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        notesCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

        contactListView.setItems(contactData.getContacts());
    }

    @FXML
    public void showAddContactDialogue() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit selected contact");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addOrEditDialogue.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        DialogueController dialogueController = loader.getController();

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        //BooleanBiding used to disable button if any field is empty
        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
                .bind(Bindings.createBooleanBinding(
                        dialogueController::checkIfAnyFieldIsEmpty,
                        dialogueController.getFirstNameField().textProperty(),
                        dialogueController.getLastNameField().textProperty(),
                        dialogueController.getPhoneNumberField().textProperty(),
                        dialogueController.getNotesField().textProperty()
                ));

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> button = dialog.showAndWait();
        if ((button.isPresent()) && (button.get() == ButtonType.OK)) {
            Contact newContact = dialogueController.processResults(contactData);
            contactListView.getSelectionModel().select(newContact);
        }
    }

    @FXML
    public void handleSaveContacts(){
        contactData.saveContacts();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Saving changes");
        alert.setContentText("Contact data successfully saved");
        alert.showAndWait();
    }


    @FXML
    public void showEditContactDialogue(Contact oldContact) {
        if(oldContact == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No contact selected");
            alert.setContentText("Please, select a contact before proceeding");
            alert.showAndWait();
            return;
        }
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit selected contact");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addOrEditDialogue.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

        DialogueController controller = loader.getController();
        controller.showPreviousAttributes(oldContact);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        dialog.getDialogPane().lookupButton(ButtonType.OK).disableProperty()
                .bind(Bindings.createBooleanBinding(
                        controller::checkIfAnyFieldIsEmpty,
                        controller.getFirstNameField().textProperty(),
                        controller.getLastNameField().textProperty(),
                        controller.getPhoneNumberField().textProperty(),
                        controller.getNotesField().textProperty()
                ));

        Optional<ButtonType> button = dialog.showAndWait();

        if ((button.isPresent()) && (button.get() == ButtonType.OK)) {
            Contact newContact = controller.processResults(contactData, oldContact);
            contactListView.getSelectionModel().select(newContact);
        }
    }

    @FXML
    public void handleEditSelection(){
        showEditContactDialogue(contactListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void handleDeleteSelection(){
        deleteContact(contactListView.getSelectionModel().getSelectedItem());
    }

    @FXML
    public void deleteContact(Contact contact){
        if(contact == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No contact selected");
            alert.setContentText("Please, select a contact before proceeding");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Delete " + contact.getFirstName() + " " + contact.getLastName());
        alert.setContentText("Are you sure you want to delete " + contact.getFirstName() + " " + contact.getLastName() + "?");

        Optional<ButtonType> button = alert.showAndWait();
        if(button.isPresent() && button.get() == ButtonType.OK) {
            contactData.deleteContact(contact);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        Contact contact = contactListView.getSelectionModel().getSelectedItem();
        final KeyCodeCombination save = new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN);
        if(contact != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteContact(contact);
            } else if(save.match(keyEvent)){
                handleSaveContacts();
            }
        }
    }


    @FXML
    public void handleCloseButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Exiting program");
        alert.setContentText("Are you sure you want to exit? Any non saved action will be deleted permanently");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get().equals(ButtonType.OK)){
            Platform.exit();
        }
    }
}
