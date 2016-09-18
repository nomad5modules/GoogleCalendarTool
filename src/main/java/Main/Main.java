package Main;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class Main extends Application
{

    /**
     * Application start
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        URL    res  = getClass().getResource("/sample.fxml");
        Parent root = FXMLLoader.load(res);
        primaryStage.setTitle("Google Calendar Tool");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * The Main main method main :)
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
