package br.com.pedropareschi.contactAgenda;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;


public class DialogueController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField notesField;

    @FXML
    public Contact processResults(ContactData contactData){
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        Contact contact = new Contact(firstName, lastName, phoneNumber, notes);
        contactData.addContact(contact);
        return contact;
    }
    @FXML
    public Contact processResults(ContactData contactData, Contact oldContact){
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String notes = notesField.getText().trim();

        Contact newContact = new Contact(firstName, lastName, phoneNumber, notes);
        contactData.editContact(oldContact, newContact);
        return newContact;
    }

    @FXML
    public void showPreviousAttributes(Contact contact){
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        notesField.setText(contact.getNotes());
    }

    public boolean checkIfAnyFieldIsEmpty(){
        boolean firstNameIsEmpty = firstNameField.getText().isEmpty() && firstNameField.getText().trim().isEmpty();
        boolean lastNameIsEmpty = lastNameField.getText().isEmpty() && lastNameField.getText().trim().isEmpty();
        boolean phoneNumberIsEmpty = phoneNumberField.getText().isEmpty() && phoneNumberField.getText().trim().isEmpty();
        boolean notesIsEmpty = notesField.getText().isEmpty() && notesField.getText().trim().isEmpty();
        return firstNameIsEmpty || lastNameIsEmpty || phoneNumberIsEmpty || notesIsEmpty;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public TextField getPhoneNumberField() {
        return phoneNumberField;
    }

    public TextField getNotesField() {
        return notesField;
    }
}
