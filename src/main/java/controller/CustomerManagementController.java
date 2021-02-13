/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Customer;
import model.Division;
import service.CustomerService;
import service.ServiceFactory;

/**
 * FXML Controller class
 *
 * @author dane
 */
public class CustomerManagementController implements Initializable {

    @FXML
    private TableColumn<Customer, Integer> idColumn;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, LocalDateTime> createDateColumn;
    @FXML
    private TableColumn<Customer, String> createdByColumn;
    @FXML
    private TableColumn<Customer, LocalDateTime> lastUpdateColumn;
    @FXML
    private TableColumn<Customer, String> lastUpdatedByColumn;
    @FXML
    private TableColumn<Division, String> DivisionColumn;
    @FXML
    private TableView<Customer> customersTableView;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        createDateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        createdByColumn.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        lastUpdateColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
        lastUpdatedByColumn.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
        DivisionColumn.setCellValueFactory(cellData -> Bindings.select(cellData.getValue(), "division", "division"));
        
        refreshCustomers();
    }    
    
    private void refreshCustomers() {
        try {
            List<Customer> customers = ServiceFactory.getCustomerInstance().getAll();
            customersTableView.setItems(FXCollections.observableArrayList(customers));
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setContentText("An error occured with the application. Please restart.");
            alert.setTitle("Application Error");
            alert.showAndWait();
        }
    }

    @FXML
    private void addCustomer(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/add_customer.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void updateCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/update_customer.fxml"));
            Parent updateCustomerParent = loader.load();
            UpdateCustomerController controller = loader.getController();
            controller.receiveCustomer(selectedCustomer);
            Scene modifyCustomerScene = new Scene(updateCustomerParent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(modifyCustomerScene);
            stage.show();
        }
    }

    @FXML
    private void deleteCustomer(ActionEvent event) throws IOException {
        Customer selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently delete the selected customer. Do you wish to continue?");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.setTitle("Delete Customer");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                CustomerService customerService = ServiceFactory.getCustomerInstance();
                try {
                    customerService.delete(selectedCustomer);
                    refreshCustomers();
                } catch (SQLException ex) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("An error occured with the application. Please restart.");
                    alert.setTitle("Application Error");
                    alert.showAndWait();
                } 
            }
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
}
