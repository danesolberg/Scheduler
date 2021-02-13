/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Appointment;
import model.Contact;
import service.ContactService;
import service.ServiceFactory;

/**
 * FXML Controller class
 *
 * @author dane
 */
public class ReportsController implements Initializable {

    @FXML
    private TableView monthTableView;
    @FXML
    private TableView<Appointment> contactTableView;
    @FXML
    private TableView totalTableView;
    @FXML
    private ComboBox<Contact> contactComboBox;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, String> titleColumn;
    @FXML
    private TableColumn<Appointment, String> typeColumn;
    @FXML
    private TableColumn<Appointment, String> descriptionColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeColumn;
    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdColumn.setCellValueFactory(cellData -> Bindings.select(cellData.getValue(), "customer", "customerId"));
        
        Format dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").toFormat();
        startTimeColumn.setCellFactory(new ColumnFormatter<>(dateTimeFormat));
        endTimeColumn.setCellFactory(new ColumnFormatter<>(dateTimeFormat));
        
        try {
            populateReports(monthTableView, ServiceFactory.getAppointmentInstance().aggregateByTypeMonth());
            populateReports(totalTableView, ServiceFactory.getAppointmentInstance().aggregateByCustomer());
            
            ContactService contactService = ServiceFactory.getContactInstance();
            contactComboBox.setConverter(ModelConverter.contactConverter);
            List<Contact> contacts = contactService.getAll();
            ObservableList observableContacts = FXCollections.observableArrayList(contacts);
            contactComboBox.setItems(observableContacts);
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    /**
     * Pull ResultSet objects from the repository and insert the contents into the
     * reporting TableViews
     * @param tableview
     * @param rs
     * @throws SQLException
     */
    private void populateReports(TableView tableview, ResultSet rs) throws SQLException {
        if (rs != null) {
            ObservableList<ObservableList> data = FXCollections.observableArrayList();
            for (int i=0; i<rs.getMetaData().getColumnCount(); i++) {
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnLabel(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>() {                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });
                tableview.getColumns().addAll(col); 
                System.out.println("Column [" + i + "] ");
            }

            while(rs.next()){
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row );
                data.add(row);
            }

            tableview.setItems(data);
            rs.close();
        }
    }

    /**
     * EventListener registered to contactComboBox that updates the appointments
     * in contactTableView upon the selection of a new contact
     * @param event
     * @throws SQLException
     */
    @FXML
    private void refreshSchedule(ActionEvent event) throws SQLException {
        Contact contact = contactComboBox.getSelectionModel().getSelectedItem();
        if (contact != null) {
            List<Appointment> appointments = ServiceFactory.getAppointmentInstance().getByContact(contact.getContactId());
            contactTableView.setItems(FXCollections.observableArrayList(appointments));   
        } else {
            contactTableView.getItems().clear();
        }
    }
}
