package ui;

import dao.UserDAO;
import model.User;
import service.DispatchService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import service.impl.DispatchServiceImpl;

import java.util.List;

public class SellerUI {
    private User currentUser;
    private DispatchService dispatchService;
    private UserDAO userDAO;

    public SellerUI(User user) {
        this.currentUser = user;
        this.dispatchService = new DispatchServiceImpl();
        this.userDAO = new UserDAO();
    }

    public void start(Stage stage) {
        stage.setTitle("Seller Dashboard - " + currentUser.getUsername());

        Label titleLabel = new Label("Create New Dispatch");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 10;");

        // Form Fields
        Label massLabel = new Label("Product Mass (tons):");
        TextField massField = new TextField();

        Label transLabel = new Label("Select Transporter:");
        ComboBox<String> transCombo = new ComboBox<>();
        List<User> transporters = userDAO.getUsersByRole("Transporter");
        for (User u : transporters)
            transCombo.getItems().add(u.getId() + ": " + u.getUsername());

        Label destLabel = new Label("Select Destination:");
        ComboBox<String> destCombo = new ComboBox<>();
        List<User> destinations = userDAO.getUsersByRole("Destination");
        for (User u : destinations)
            destCombo.getItems().add(u.getId() + ": " + u.getUsername());

        Button createButton = new Button("Create Dispatch");
        createButton.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 5;");

        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-text-fill: white;");

        massLabel.setStyle("-fx-text-fill: #ecf0f1;");
        transLabel.setStyle("-fx-text-fill: #ecf0f1;");
        destLabel.setStyle("-fx-text-fill: #ecf0f1;");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(massLabel, 0, 0);
        grid.add(massField, 1, 0);
        grid.add(transLabel, 0, 1);
        grid.add(transCombo, 1, 1);
        grid.add(destLabel, 0, 2);
        grid.add(destCombo, 1, 2);
        grid.add(createButton, 1, 3);

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, grid, msgLabel, logoutButton);
        root.setStyle("-fx-background-color: #2c3e50;");

        // Actions
        createButton.setOnAction(e -> {
            try {
                double mass = Double.parseDouble(massField.getText());

                String transSelection = transCombo.getValue();
                String destSelection = destCombo.getValue();

                if (transSelection == null || destSelection == null) {
                    msgLabel.setText("Please select Transporter and Destination.");
                    msgLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                int transId = Integer.parseInt(transSelection.split(":")[0]);
                int destId = Integer.parseInt(destSelection.split(":")[0]);

                dispatchService.createDispatch(mass, currentUser.getId(), transId, destId);
                msgLabel.setText("Dispatch Created Successfully!");
                msgLabel.setStyle("-fx-text-fill: green;");

                // Clear fields
                massField.clear();
                transCombo.getSelectionModel().clearSelection();
                destCombo.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                msgLabel.setText("Invalid Mass. Please enter a number.");
                msgLabel.setStyle("-fx-text-fill: red;");
            } catch (Exception ex) {
                msgLabel.setText("Error: " + ex.getMessage());
                msgLabel.setStyle("-fx-text-fill: red;");
                ex.printStackTrace();
            }
        });

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.show();
    }
}
