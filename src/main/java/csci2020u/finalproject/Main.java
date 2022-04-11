package csci2020u.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;


public class Main extends Application {
    /**
     *
     * @param primaryStage function to transition between stages
     * @throws Exception if scene isn't loaded correctly
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client.fxml")));
        primaryStage.setTitle("GitMock Desktop");
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("file:gitmock-logo.png"));
        scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("client.css")).toExternalForm());
        primaryStage.show();
    }

    /**
     *
     * @param args contains the supplied command-line arguments as an array of String objects.
     */
    public static void main(String[] args) {
        launch(args);
    }
}