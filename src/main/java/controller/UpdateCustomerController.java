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
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;
import service.CountryService;
import service.CustomerService;
import service.DivisionService;
import service.ServiceFactory;

/**
 * FXML Controller class
 *
 * @author dane
 */
public class UpdateCustomerController implements Initializable {
    @FXML
    private TextField customerIdTextField;
    @FXML
    private TextField customerNameTextField;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private ComboBox<Division> divisionComboBox;
    @FXML
    private ComboBox<Country> countryComboBox;
    private List<Division> divisions;
    private Customer customerToModify;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CountryService countryService = ServiceFactory.getCountryInstance();
        DivisionService divisionService = ServiceFactory.getDivisionInstance();
        countryComboBox.setConverter(ModelConverter.countryConverter);
        divisionComboBox.setConverter(ModelConverter.divisionConverter);
        
        try {
            List<Country> countries = countryService.getAll();
            divisions = divisionService.getAll();
            ObservableList observableCountries = FXCollections.observableArrayList(countries);
            countryComboBox.setItems(observableCountries);
        } catch (SQLException ex) {
            Logger.getLogger(UpdateCustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Saves the updated Customer instance to the connected repository
     * @param event
     * @throws IOException
     */
    @FXML
    private void save(ActionEvent event) throws IOException {
        LocalDateTime timestamp = LocalDateTime.now();
        String user = App.getCurrentUser().getUserName();
        
        int id = customerToModify.getCustomerId();
        String name = customerNameTextField.getText().trim();
        String address = addressTextField.getText().trim();
        String postalCode = postalCodeTextField.getText().trim();
        String phone = phoneTextField.getText().trim();
        Division division = divisionComboBox.getSelectionModel().getSelectedItem();

        Customer newCustomer = new Customer(
            id,
            name,
            address,
            postalCode,
            phone,
            null,
            null,
            timestamp,
            user,
            division
        );
        
        CustomerService customerService = ServiceFactory.getCustomerInstance();
        try {
            customerService.update(newCustomer);
            back(event);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setContentText("An error occured with the application. Please restart.");
            alert.setTitle("Application Error");
            alert.showAndWait();
        } 
    }

    @FXML
    private void back(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/customer_management.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Populates combo box of first-level divisions upon selection of a country
     * @param event
     */
    @FXML
    private void handleCountrySelection(ActionEvent event) {
        List<Division> filteredDivisions = divisions.stream().filter(division -> division.getCountry()
            .getCountryId() == countryComboBox.getSelectionModel().getSelectedItem()
            .getCountryId()).collect(Collectors.toList());
        ObservableList observableDivisions = FXCollections.observableArrayList(filteredDivisions);
        divisionComboBox.setItems(observableDivisions);
    }
    
    /**
     * Method to process the original Customer data transmitted from the
     * CustomerManagementController upon JavaFX scene change, in order to update
     * the data
     * @param customer
     */
    public void receiveCustomer(Customer customer) {
        customerToModify = customer;
        customerIdTextField.setText(Integer.toString(customer.getCustomerId()));
        customerNameTextField.setText(customer.getCustomerName());
        addressTextField.setText(customer.getAddress());
        postalCodeTextField.setText(customer.getPostalCode());
        phoneTextField.setText(customer.getPhone());
        countryComboBox.getSelectionModel().select(customer.getDivision().getCountry());
        divisionComboBox.getSelectionModel().select(customer.getDivision());
    }
}
