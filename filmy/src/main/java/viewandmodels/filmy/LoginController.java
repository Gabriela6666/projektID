package viewandmodels.filmy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController extends Application {

    private Main mainApp;

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        VBox vbox = new VBox();

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (authenticate(username, password)) {
                showAlert("Login Successful", "Welcome, " + username + "!");
                primaryStage.close();
                mainApp.showMainWindow(new Stage());
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        });

        vbox.getChildren().addAll(usernameField, passwordField, loginButton);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
//        String query = "SELECT haslo FROM public.uzytkownicy WHERE nazwa = ?";
//        try (Connection conn = Database.connect();
//             PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setString(1, username);
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                String hashedPassword = rs.getString("haslo");
//                return checkPassword(password, hashedPassword);
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        return false;
        return true;
    }

    private boolean checkPassword(String password, String hashedPassword) {
        // Implement password checking logic (e.g., hashing the input password and comparing with the stored hash)
        return password.equals(hashedPassword);  // Simplified for illustration purposes
//        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
