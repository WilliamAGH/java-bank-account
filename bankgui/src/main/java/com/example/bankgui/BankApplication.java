package com.example.bankgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import atlantafx.base.theme.PrimerLight;

/**
 * Main entry point for the Bank Account Management JavaFX application
 * This also loads the FXML view and sets up the primary stage
 * @author William Callahan
 * @since April 29, 2025
 */
public class BankApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // apply the initial/default theme (light) to the application
        // TODO: I wonder if there is a way to make this detect the system theme and apply that instead?
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        FXMLLoader fxmlLoader = new FXMLLoader(BankApplication.class.getResource("bank-account-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 500);

        // TODO: Explore adding an application icon later

        stage.setTitle("Bank of Java");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * the main method, launching the JavaFX application
     */
    public static void main(String[] args) {
        launch();
    }
}