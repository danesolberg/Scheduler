package app;

import dal.DaoFactory;
import dal.DaoFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import model.User;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    /**
     * This instance of the DaoFactory interface creates all the Data Access
     * Objects (DAOs) throughout the application that communicate with the
     * selected repository. Currently the app uses the concrete MySql
     * implementation for saving data to a remote MySql database.
     */
    private static DaoFactory daoFactory = DaoFactory.getDaoFactory(DaoFactory.MYSQL);

    /**
     * The current signed in user.  Set from LoginController.
     */
    private static User currentUser;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/views/login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * 
     * @return the selected DaoFactory instance used to generate DAOs
     */
    public static DaoFactory getDaoFactory() {
        return daoFactory;
    }
    
    /**
     * 
     * @return the current signed in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     *
     * @param user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}