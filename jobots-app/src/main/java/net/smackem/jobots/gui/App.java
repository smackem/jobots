package net.smackem.jobots.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        final Scene scene = new Scene(loadFXML("board"), 800, 600);
        stage.setScene(scene);
        stage.setTitle("jobots");
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        final URL fxmlUrl = App.class.getResource(fxml + ".fxml");
        final FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}

