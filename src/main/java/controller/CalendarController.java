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
import java.text.Format;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.next;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;
import service.AppointmentService;
import service.ServiceFactory;

/**
 * FXML Controller class
 *
 * @author dane
 */
public class CalendarController implements Initializable {

    @FXML
    private TableView<Appointment> appointmentsTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, String> locationColumn;
    @FXML
    private TableColumn<Contact, String> contactColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeColumn;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private TextField currentCalendarTextField;
    private ToggleGroup calendarTypeToggleGroup;

    /**
     * Maintains the start of the current week shown by the calendar
     */
    private LocalDate currentWeek;

    /**
     * Maintains the start of the current month shown by the calendar
     */
    private LocalDate currentMonth;
    private static final int WEEK = 1;
    private static final int MONTH = 2;
    private int calendarType;
    @FXML
    private Label calendarHeaderLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        calendarTypeToggleGroup = new ToggleGroup();
        weekRadioButton.setToggleGroup(calendarTypeToggleGroup);
        monthRadioButton.setToggleGroup(calendarTypeToggleGroup);
        weekRadioButton.setSelected(true);
        calendarType = WEEK;
        
        LocalDate today = LocalDate.now();
        currentWeek = today.with(previous(SUNDAY));
        currentMonth = today.withDayOfMonth(1);
        
        setCalendarRange();
        setCalendarHeader();
        
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactColumn.setCellValueFactory(cellData -> Bindings.select(cellData.getValue(), "contact", "contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdColumn.setCellValueFactory(cellData -> Bindings.select(cellData.getValue(), "customer", "customerName"));
        
        Format dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").toFormat();
        startTimeColumn.setCellFactory(new ColumnFormatter<>(dateTimeFormat));
        endTimeColumn.setCellFactory(new ColumnFormatter<>(dateTimeFormat));
        refreshAppointments();
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Updates the time period and contents of the calendar based on the user's
     * week/month selection
     * @param event
     */
    @FXML
    private void handleCalendarType(ActionEvent event) {
        if (calendarTypeToggleGroup.getSelectedToggle().equals(weekRadioButton)) {
            calendarType = WEEK;
            currentWeek = currentMonth.withDayOfMonth(1).plusDays(1).with(previous(SUNDAY));
        } else {
            calendarType = MONTH;
            currentMonth = currentWeek.withDayOfMonth(1);
        }
        setCalendarRange();
        setCalendarHeader();
        refreshAppointments();
    }

    /**
     * Shifts the calendar time window one period forward
     * @param event
     */
    @FXML
    private void nextCalendar(MouseEvent event) {
        if (calendarType == WEEK) {
            currentWeek = currentWeek.with(next(SUNDAY));
        } else {
            currentMonth = currentMonth.withDayOfMonth(
                currentMonth.getMonth().length(currentMonth.isLeapYear())).plusDays(1);
        }
        setCalendarRange();
        setCalendarHeader();
        refreshAppointments();
    }

    /**
     * Shifts the calendar time window one period backward
     * @param event
     */
    @FXML
    private void previousCalendar(MouseEvent event) {
        if (calendarType == WEEK) {
            currentWeek = currentWeek.with(previous(SUNDAY));
        } else {
            currentMonth = currentMonth.minusDays(1).withDayOfMonth(1);
        }
        setCalendarRange();
        setCalendarHeader();
        refreshAppointments();
    }

    @FXML
    private void addAppointment(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/add_appointment.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void updateAppointment(ActionEvent event) throws IOException {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        
        if (selectedAppointment != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/update_appointment.fxml"));
            Parent updateAppointmentParent = loader.load();
            UpdateAppointmentController controller = loader.getController();
            controller.receiveAppointment(selectedAppointment);
            Scene modifyAppointmentScene = new Scene(updateAppointmentParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(modifyAppointmentScene);
            stage.show();
        }
    }

    /**
     * Removes the appointment the corresponds to the calendar TableView selection
     * from the repository
     * @param event
     */
    @FXML
    private void deleteAppointment(ActionEvent event) {
        Appointment selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        if (selectedAppointment != null) {
            alert.setContentText("Are you sure you want to delete Appointment " + selectedAppointment.getAppointmentId() + " \"" + selectedAppointment.getType() + "\"?");
            alert.setTitle("Delete Customer");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                AppointmentService appointmentService = ServiceFactory.getAppointmentInstance();
                try {
                    appointmentService.delete(selectedAppointment);
                    refreshAppointments();
                } catch (SQLException ex) {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setContentText("An error occured with the application. Please restart.");
                    alert.setTitle("Application Error");
                    alert.showAndWait();
                } 
            }
        }
    }
    
    /**
     * Updates the View to show the current calendar time window
     */
    private void setCalendarRange() {
        if (calendarType == WEEK) {
            currentCalendarTextField.setText(currentWeek.toString() + "   -   " + currentWeek.with(next(SATURDAY)));
        } else {
            currentCalendarTextField.setText(currentMonth.toString() + "   -   " + currentMonth.withDayOfMonth(
                currentMonth.getMonth().length(currentMonth.isLeapYear())));
        }
    }
    
    /**
     * Updates the View to show the current calendar month
     */
    private void setCalendarHeader() {
        if (calendarType == WEEK) {
            calendarHeaderLabel.setText(currentWeek.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentWeek.getYear());
        } else {
            calendarHeaderLabel.setText(currentMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentMonth.getYear());
        }
    }
    
    /**
     * Updates the contents of the calendar as specified by user interaction
     */
    private void refreshAppointments() {
        int user = App.getCurrentUser().getUserId();
        List<Appointment> appointments;
        try {
            if (calendarType == WEEK) {
                appointments = ServiceFactory.getAppointmentInstance().getByUser(
                    user,
                    currentWeek.atTime(LocalTime.MIN),
                    currentWeek.with(next(SATURDAY)).atTime(LocalTime.MAX)
                );
            } else {
                appointments = ServiceFactory.getAppointmentInstance().getByUser(
                    user,
                    currentMonth.atTime(LocalTime.MIN),
                    currentMonth.withDayOfMonth(currentMonth.getMonth().
                        length(currentMonth.isLeapYear())).atTime(LocalTime.MAX)
                );
            }
            if (!appointments.isEmpty()) {
                appointmentsTableView.setItems(FXCollections.observableArrayList(appointments));
            } else {
                appointmentsTableView.getItems().clear();
            }
            
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error occured with the application. Please restart.");
            alert.setTitle("Application Error");
            alert.showAndWait();
        }
    }
}
