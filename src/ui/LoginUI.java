package ui;

import dao.UserDAO;
import model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {

    private UserDAO userDAO = new UserDAO();

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dispatch System - Login");

        // UI Components
        Label titleLabel = new Label("Dispatch Management System");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 10;");

        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-text-fill: #ecf0f1;");
        TextField userField = new TextField();
        userField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #ecf0f1;");
        PasswordField passField = new PasswordField();
        passField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 20;");

        Button signupButton = new Button("Sign Up");
        signupButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #3498db; -fx-border-color: #3498db; -fx-border-radius: 20; -fx-padding: 8 20;");

        // Layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.add(userLabel, 0, 1);
        grid.add(userField, 1, 1);
        grid.add(passLabel, 0, 2);
        grid.add(passField, 1, 2);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, grid, loginButton, signupButton, msgLabel);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #000000);");

        // Event Handling
        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setText("Please fill all fields.");
                return;
            }

            User user = userDAO.authenticate(username, password);

            if (user != null) {
                msgLabel.setText("Login Successful!");
                openRoleUI(primaryStage, user);
            } else {
                msgLabel.setText("Invalid credentials.");
            }
        });

        signupButton.setOnAction(e -> showSignupScreen(primaryStage));

        Scene scene = new Scene(root, 400, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSignupScreen(Stage stage) {
        stage.setTitle("Dispatch System - Sign Up");

        Label titleLabel = new Label("Create New Account");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label userLabel = new Label("Username:");
        userLabel.setStyle("-fx-text-fill: #ecf0f1;");
        TextField userField = new TextField();
        userField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Label passLabel = new Label("Password:");
        passLabel.setStyle("-fx-text-fill: #ecf0f1;");
        PasswordField passField = new PasswordField();
        passField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");

        Label roleLabel = new Label("Role:");
        roleLabel.setStyle("-fx-text-fill: #ecf0f1;");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Seller", "Transporter", "Destination", "Admin");
        roleBox.setValue("Seller");
        roleBox.setStyle("-fx-background-radius: 5;");

        Label secretLabel = new Label("Admin Code:");
        secretLabel.setStyle("-fx-text-fill: #ecf0f1;");
        PasswordField secretField = new PasswordField();
        secretField.setStyle("-fx-background-radius: 5; -fx-padding: 8;");
        secretLabel.setVisible(false);
        secretField.setVisible(false);

        roleBox.setOnAction(e -> {
            boolean isAdmin = "Admin".equals(roleBox.getValue());
            secretLabel.setVisible(isAdmin);
            secretField.setVisible(isAdmin);
        });

        Button registerButton = new Button("Register");
        registerButton.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 30; -fx-background-radius: 20;");

        Button backButton = new Button("Back to Login");
        backButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 20;");

        Label msgLabel = new Label();

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(roleBox, 1, 2);
        grid.add(secretLabel, 0, 3);
        grid.add(secretField, 1, 3);

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, grid, registerButton, backButton, msgLabel);
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #2c3e50, #000000);");

        registerButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setText("Fill all fields.");
                msgLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if ("Admin".equals(role)) {
                if (!"1$isbester".equals(secretField.getText())) {
                    msgLabel.setText("Invalid Admin Code.");
                    msgLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
            }

            if (userDAO.isUsernameTaken(username)) {
                msgLabel.setText("Username already exists.");
                msgLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            User newUser;
            switch (role) {
                case "Admin":
                    newUser = new model.Admin(0, username, password);
                    break;
                case "Seller":
                    newUser = new model.Seller(0, username, password);
                    break;
                case "Transporter":
                    newUser = new model.Transporter(0, username, password);
                    break;
                case "Destination":
                    newUser = new model.Destination(0, username, password);
                    break;
                default:
                    return;
            }

            if (userDAO.register(newUser)) {
                msgLabel.setText("Registration Successful!");
                msgLabel.setStyle("-fx-text-fill: green;");
            } else {
                msgLabel.setText("Registration failed.");
                msgLabel.setStyle("-fx-text-fill: red;");
            }
        });

        backButton.setOnAction(e -> start(stage));

        Scene scene = new Scene(root, 400, 450);
        stage.setScene(scene);
    }

    private void openRoleUI(Stage stage, User user) {
        switch (user.getRole()) {
            case "Seller":
                new SellerUI(user).start(stage);
                break;
            case "Transporter":
                new TransporterUI(user).start(stage);
                break;
            case "Destination":
                new DestinationUI(user).start(stage);
                break;
            case "Admin":
                new AdminUI(user).start(stage);
                break;
            default:
                System.out.println("Unknown Role");
        }
    }
}
