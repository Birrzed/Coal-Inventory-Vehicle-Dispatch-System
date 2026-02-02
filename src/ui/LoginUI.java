package ui;

import dao.UserDAO;
import model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginUI {

    private UserDAO userDAO = new UserDAO();

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dispatch System - Login");

        // UI Components
        Label titleLabel = new Label("COAL DISPATCH");
        titleLabel.setStyle(StyleHelper.TITLE_TEXT);

        Label subtitleLabel = new Label("Management System");
        subtitleLabel.setStyle(StyleHelper.NORMAL_TEXT);

        Label userLabel = new Label("Username");
        userLabel.setStyle(StyleHelper.LABEL_STYLE);
        TextField userField = new TextField();
        userField.setPromptText("Enter your username");
        userField.setStyle(StyleHelper.TEXT_FIELD);

        Label passLabel = new Label("Password");
        passLabel.setStyle(StyleHelper.LABEL_STYLE);
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter your password");
        passField.setStyle(StyleHelper.TEXT_FIELD);

        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-text-fill: #ef4444;");

        Button loginButton = new Button("Login");
        loginButton.setStyle(StyleHelper.BUTTON_PRIMARY);
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Button signupButton = new Button("Create Account");
        signupButton.setStyle(StyleHelper.BUTTON_SECONDARY);
        signupButton.setMaxWidth(Double.MAX_VALUE);

        // Form Layout (Glass Panel)
        VBox formCard = new VBox(15);
        formCard.setStyle(StyleHelper.GLASS_PANEL);
        formCard.setPrefWidth(350);
        formCard.setMaxWidth(350);
        formCard.getChildren().addAll(userLabel, userField, passLabel, passField, loginButton, signupButton, msgLabel);

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setStyle(StyleHelper.MAIN_BG);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(new VBox(5, titleLabel, subtitleLabel), formCard);
        ((VBox) root.getChildren().get(0)).setAlignment(Pos.CENTER);

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

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showSignupScreen(Stage stage) {
        stage.setTitle("Dispatch System - Sign Up");

        Label titleLabel = new Label("Join the System");
        titleLabel.setStyle(StyleHelper.TITLE_TEXT);

        Label userLabel = new Label("Username");
        userLabel.setStyle(StyleHelper.LABEL_STYLE);
        TextField userField = new TextField();
        userField.setStyle(StyleHelper.TEXT_FIELD);

        Label passLabel = new Label("Password");
        passLabel.setStyle(StyleHelper.LABEL_STYLE);
        PasswordField passField = new PasswordField();
        passField.setStyle(StyleHelper.TEXT_FIELD);

        Label roleLabel = new Label("Role");
        roleLabel.setStyle(StyleHelper.LABEL_STYLE);
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("Seller", "Transporter", "Destination", "Admin");
        roleBox.setValue("Seller");
        roleBox.setStyle(StyleHelper.TEXT_FIELD);
        roleBox.setMaxWidth(Double.MAX_VALUE);

        Label secretLabel = new Label("Admin Code");
        secretLabel.setStyle(StyleHelper.LABEL_STYLE);
        PasswordField secretField = new PasswordField();
        secretField.setStyle(StyleHelper.TEXT_FIELD);
        secretLabel.setVisible(false);
        secretField.setVisible(false);

        roleBox.setOnAction(e -> {
            boolean isAdmin = "Admin".equals(roleBox.getValue());
            secretLabel.setVisible(isAdmin);
            secretField.setVisible(isAdmin);
        });

        Button registerButton = new Button("Register Now");
        registerButton.setStyle(StyleHelper.BUTTON_PRIMARY);
        registerButton.setMaxWidth(Double.MAX_VALUE);

        Button backButton = new Button("Back to Login");
        backButton.setStyle(StyleHelper.BUTTON_SECONDARY);
        backButton.setMaxWidth(Double.MAX_VALUE);

        Label msgLabel = new Label();

        VBox formCard = new VBox(12);
        formCard.setStyle(StyleHelper.GLASS_PANEL);
        formCard.setPrefWidth(350);
        formCard.getChildren().addAll(userLabel, userField, passLabel, passField, roleLabel, roleBox, secretLabel,
                secretField, registerButton, backButton, msgLabel);

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setStyle(StyleHelper.MAIN_BG);
        root.setPadding(new Insets(40));
        root.getChildren().addAll(titleLabel, formCard);

        registerButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();
            String role = roleBox.getValue();

            if (username.isEmpty() || password.isEmpty()) {
                msgLabel.setText("Fill all fields.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            if ("Admin".equals(role)) {
                if (!"1$isbester".equals(secretField.getText())) {
                    msgLabel.setText("Invalid Admin Code.");
                    msgLabel.setStyle("-fx-text-fill: #ef4444;");
                    return;
                }
            }

            if (userDAO.isUsernameTaken(username)) {
                msgLabel.setText("Username already exists.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
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
                msgLabel.setStyle("-fx-text-fill: #22c55e;");
            } else {
                msgLabel.setText("Registration failed.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
            }
        });

        backButton.setOnAction(e -> start(stage));

        Scene scene = new Scene(root, 500, 700);
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
