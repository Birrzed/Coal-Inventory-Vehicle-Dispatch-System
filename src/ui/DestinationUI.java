package ui;

import model.Dispatch;
import model.User;
import service.DispatchService;
import service.impl.DispatchServiceImpl;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DestinationUI {
    private User currentUser;
    private DispatchService dispatchService;

    public DestinationUI(User user) {
        this.currentUser = user;
        this.dispatchService = new DispatchServiceImpl();
    }

    public void start(Stage stage) {
        stage.setTitle("Destination Dashboard - " + currentUser.getUsername());

        // Dynamic title based on authenticated user session
        Label titleLabel = new Label("Incoming Dispatches");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 10;");

        TableView<Dispatch> table = new TableView<>();

        TableColumn<Dispatch, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Dispatch, Double> massCol = new TableColumn<>("Mass (Tons)");
        massCol.setCellValueFactory(new PropertyValueFactory<>("productMass"));

        TableColumn<Dispatch, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, massCol, statusCol);

        Button confirmButton = new Button("Confirm Arrival (End Transport)");
        confirmButton.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 5;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 5;");

        Label msgLabel = new Label();
        msgLabel.setStyle("-fx-text-fill: white;");

        refreshTable(table);

        confirmButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msgLabel.setText("Please select a dispatch.");
                msgLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            if (!"In Transit".equals(selected.getStatus())) {
                msgLabel.setText("Only 'In Transit' dispatches can be confirmed as Arrived.");
                msgLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            dispatchService.confirmArrival(selected.getId());
            msgLabel.setText("Arrival Confirmed! Status: Delivered.");
            msgLabel.setStyle("-fx-text-fill: green;");
            refreshTable(table);
        });

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, table, confirmButton, msgLabel, logoutButton);
        root.setStyle("-fx-background-color: #2c3e50;");

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshTable(TableView<Dispatch> table) {
        table.setItems(FXCollections.observableArrayList(
                dispatchService.getDispatchesForUser("Destination", currentUser.getId())));
    }
}
