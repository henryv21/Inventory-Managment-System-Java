package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class launches the main menu
 */
public class Main extends Application {
    public static Stage stage;

    /**
     * The main menu scene is loaded when the application starts.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        Parent homeView = FXMLLoader.load(getClass().getResource("/Views/MainMenu.fxml"));
        stage.setScene(new Scene(homeView));
        stage.setTitle("Inventory Management System");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
