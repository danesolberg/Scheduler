/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import app.App;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;
import service.AppointmentService;
import service.ContactService;
import service.CustomerService;
import service.CustomerUnavailableException;
import service.InvalidAppointmentException;
import service.OutsideBusinessHoursException;
import service.ServiceFactory;

/**
 * FXML Controller class
 *
 * @author dane
 */
public class UpdateAppointmentController implements Initializable {

    @FXML
    private TextField appointmentIdTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField locationTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private TextField startTextField;
    @FXML
    private TextField endTextField;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private RadioButton startAMRadioButton;
    @FXML
    private RadioButton startPMRadioButton;
    @FXML
    private RadioButton endAMRadioButton;
    @FXML
    private RadioButton endPMRadioButton;
    private Appointment appointmentToModify;
    private ToggleGroup startTimeToggleGroup;
    private ToggleGroup endTimeToggleGroup;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        startTimeToggleGroup = new ToggleGroup();
        startAMRadioButton.setToggleGroup(startTimeToggleGroup);
        startPMRadioButton.setToggleGroup(startTimeToggleGroup);
        endTimeToggleGroup = new ToggleGroup();
        endAMRadioButton.setToggleGroup(endTimeToggleGroup);
        endPMRadioButton.setToggleGroup(endTimeToggleGroup);
        
        CustomerService customerService = ServiceFactory.getCustomerInstance();
        ContactService contactService = ServiceFactory.getContactInstance();
        customerComboBox.setConverter(ModelConverter.customerConverter);
        contactComboBox.setConverter(ModelConverter.contactConverter);
        
        try {
            List<Customer> customers = customerService.getAll();
            List<Contact> contacts = contactService.getAll();
            ObservableList observableCustomers = FXCollections.observableArrayList(customers);
            customerComboBox.setItems(observableCustomers);
            ObservableList observableContacts = FXCollections.observableArrayList(contacts);
            contactComboBox.setItems(observableContacts);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateAppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    /**
     * Saves the updated Appointment instance to the connected repository
     * @param event
     * @throws IOException
     */
    @FXML
    private void save(ActionEvent event) throws IOException {
        LocalDateTime timestamp = LocalDateTime.now();
        User user = App.getCurrentUser();
        
        RadioButton startRadio = (RadioButton)startTimeToggleGroup.getSelectedToggle();
        RadioButton endRadio = (RadioButton)endTimeToggleGroup.getSelectedToggle();
        LocalTime startTime = LocalTime.parse(startTextField.getText().trim() + startRadio.getText(), DateTimeFormatter.ofPattern("h:mma", Locale.US));
        LocalTime endTime = LocalTime.parse(endTextField.getText().trim() + endRadio.getText(), DateTimeFormatter.ofPattern("h:mma", Locale.US));
        
        int id = appointmentToModify.getAppointmentId();
        String title = titleTextField.getText().trim();
        String description = descriptionTextField.getText().trim();
        String location = locationTextField.getText().trim();
        String type = typeTextField.getText().trim();
        LocalDate date = appointmentDatePicker.getValue();
        LocalDateTime start = date.atTime(startTime);
        LocalDateTime end = date.atTime(endTime);
        Customer customer = customerComboBox.getSelectionModel().getSelectedItem();
        Contact contact = contactComboBox.getSelectionModel().getSelectedItem();
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        Appointment newAppointment = new Appointment(
            id,
            title,
            description,
            location,
            type,
            start,
            end,
            null,
            null,
            timestamp,
            user.getUserName(),
            customer,
            user,
            contact
        );
        
        AppointmentService appointmentService = ServiceFactory.getAppointmentInstance();
        try {
            appointmentService.update(newAppointment);
            back(event);
        } catch (CustomerUnavailableException | OutsideBusinessHoursException | InvalidAppointmentException ex) {
            alert.setContentText(ex.getMessage());
            alert.setTitle("Appointment Error");
            alert.showAndWait();
        } catch (SQLException ex) {
            alert.setContentText("An error occured with the application. Please restart.");
            alert.setTitle("Application Error");
            alert.showAndWait();
        } 
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/calendar.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**
     * Method to process the original Appointment data transmitted from the
     * CalendarController upon JavaFX scene change, in order to update the data
     * @param appointment
     */
    public void receiveAppointment(Appointment appointment) {
        appointmentToModify = appointment;
        appointmentIdTextField.setText(Integer.toString(appointment.getAppointmentId()));
        titleTextField.setText(appointment.getTitle());
        descriptionTextField.setText(appointment.getDescription());
        locationTextField.setText(appointment.getLocation());
        typeTextField.setText(appointment.getType());
        appointmentDatePicker.setValue(appointment.getStart().toLocalDate());
        startTextField.setText(appointment.getStart().toLocalTime().format(DateTimeFormatter.ofPattern("h:mm")));
        endTextField.setText(appointment.getEnd().toLocalTime().format(DateTimeFormatter.ofPattern("h:mm")));
        
        if (appointment.getStart().toLocalTime().isBefore(LocalTime.NOON)) {
            startAMRadioButton.setSelected(true);
        } else {
            startPMRadioButton.setSelected(true);
        }
        if (appointment.getEnd().toLocalTime().isBefore(LocalTime.NOON)) {
            endAMRadioButton.setSelected(true);
        } else {
            endPMRadioButton.setSelected(true);
        }
        
        customerComboBox.getSelectionModel().select(appointment.getCustomer());
        contactComboBox.getSelectionModel().select(appointment.getContact());
    }
}
