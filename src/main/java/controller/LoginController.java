/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import app.App;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javax.security.auth.login.FailedLoginException;
import model.Appointment;
import model.User;
import service.ServiceFactory;
import service.UserService;

/**
 * FXML Controller class
 *
 * @author dane
 */
public class LoginController implements Initializable {
    @FXML
    private Label titleLabel;
    @FXML
    private Label userIdLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField userIdTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField countryTextField;
    @FXML
    private Label countryLabel;
    
    private Locale locale = Locale.getDefault();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (locale.getLanguage().equals("en")) {
            titleLabel.setText("User login");
            userIdLabel.setText("User ID");
            passwordLabel.setText("Password");
            loginButton.setText("Enter");
            countryLabel.setText("Country");
        } else if (locale.getLanguage().equals("fr")) {
            titleLabel.setText("Utilisateur en ligne");
            userIdLabel.setText("Identifiant d'utilisateur");
            passwordLabel.setText("Mot de passe");
            loginButton.setText("Entrer");
            countryLabel.setText("L'état");
        }
        
        countryTextField.setText(locale.getDisplayCountry());
    }    

    /**
     * Attempts a user login and proceeds to alert the user to application errors,
     * login errors, and upcoming appointments
     * @param event
     * @throws IOException
     */
    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        try {
            login();
            User user = App.getCurrentUser();
            if (user != null) {
                System.out.println(LocalDateTime.now());
                System.out.println(LocalDateTime.now().plusMinutes(15));
                List<Appointment> appointments = ServiceFactory.getAppointmentInstance().getUpcoming(
                    user.getUserId(),
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15)
                );
                
                alert.setTitle("Upcoming Appointments");
                if (!appointments.isEmpty()) {
                    StringBuilder content = new StringBuilder();
                    content.append("You have ").append(appointments.size()).append(" appointment(s) within 15 minutes.\n");
                    /**
                     * When the user logs in a list of all upcoming appointments
                     * within the next 15 minutes are listed.  The expression
                     * takes in an appointment as a parameter and appends the
                     * appointment's information to a StringBuilder instance.
                     */
                    appointments.forEach((appt) -> {
                        content.append("ID: ").append(appt.getAppointmentId()).append(" Date / Time: ")
                            .append(appt.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
                    });
                    alert.setContentText(content.toString());
                } else {
                    alert.setContentText("You have no upcoming appointments.");
                }
                alert.showAndWait();
                switchToHome(event);
            }
        } catch (SQLException ex) {
            alert.setAlertType(Alert.AlertType.ERROR);
            if (locale.getLanguage().equals("fr")) {
                alert.setContentText("Une erreur s'est produite avec l'application. Veuillez redémarrer.");
                alert.setTitle("Erreur d'application");
            } else {
                alert.setContentText("An error occured with the application. Please restart.");
                alert.setTitle("Application Error");
            }
            alert.showAndWait();
        } catch (NumberFormatException | FailedLoginException ex) {
            alert.setAlertType(Alert.AlertType.ERROR);
            if (locale.getLanguage().equals("fr")) {
                alert.setContentText("ID utilisateur ou mot de passe incorrect");
                alert.setTitle("Identifiant invalide");
            } else {
                alert.setContentText("Incorrect User ID or Password");
                alert.setTitle("Invalid Login");
            }
            alert.showAndWait();
        }
    }
    
    /**
     * Method to handle user login validation. Employs simple plaintext password
     * comparison from user password stored in repository. Not an example of 
     * something cryptographically secure
     * @throws FailedLoginException
     * @throws IOException
     * @throws SQLException
     */
    private void login() throws FailedLoginException, IOException, SQLException {
        UserService userService = ServiceFactory.getUserInstance();
        User user = null;
        String filename= "login_activity.txt";
        FileWriter fw = new FileWriter(filename, true);
        StringBuilder line = new StringBuilder();
        boolean failed = false;
        
        try {
            int userId = Integer.valueOf(userIdTextField.getText());
            user = userService.getById(userId);
        } catch (NumberFormatException ex) {}
        
        if (user == null) {
            System.out.println("User not found.");
            line.append("UNKNOWN USER: ").append(userIdTextField.getText()).append(" ").append(Instant.now().toString()).append("\n");
            failed = true;
        } else if (!user.getPassword().equals(passwordField.getText())) {
            System.out.println("Login failed.");
            line.append("FAIL: ").append(user.getUserName()).append(" ").append(Instant.now().toString()).append("\n");
            failed = true;
        } else {
            System.out.println("Login succeeded.");
            line.append("SUCCESS: ").append(user.getUserName()).append(" ").append(Instant.now().toString()).append("\n");
        }

        fw.write(line.toString());
        fw.close();

        if (failed == true) { throw new FailedLoginException(); }
        App.setCurrentUser(user);
    }
    
    private void switchToHome(ActionEvent event) throws IOException {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(getClass().getResource("/views/home.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
