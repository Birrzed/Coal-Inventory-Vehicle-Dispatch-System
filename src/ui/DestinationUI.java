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
import javafx.scene.layout.HBox;
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

        VBox box = new VBox(20);
        box.setPadding(new Insets(30));
        box.setStyle(StyleHelper.GLASS_PANEL);

        Label titleLabel = new Label("Incoming Dispatches");
        titleLabel.setStyle(StyleHelper.HEADER_TEXT);

        TableView<Dispatch> table = new TableView<>();
        table.setStyle(StyleHelper.TABLE_STYLE);

        TableColumn<Dispatch, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Dispatch, Double> massCol = new TableColumn<>("Mass (Tons)");
        massCol.setCellValueFactory(new PropertyValueFactory<>("productMass"));

        TableColumn<Dispatch, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, massCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER);

        Button confirmButton = new Button("Confirm Delivery (Arrival)");
        confirmButton.setStyle(StyleHelper.BUTTON_PRIMARY);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(StyleHelper.BUTTON_SECONDARY);

        actions.getChildren().addAll(confirmButton, logoutButton);

        Label msgLabel = new Label();
        msgLabel.setStyle(StyleHelper.NORMAL_TEXT);

        refreshTable(table);

        confirmButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msgLabel.setText("Please select a dispatch.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            if (!"In Transit".equals(selected.getStatus())) {
                msgLabel.setText("Only 'In Transit' dispatches can be confirmed for arrival.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            dispatchService.confirmArrival(selected.getId());
            msgLabel.setText("Arrival Confirmed! Status: Delivered.");
            msgLabel.setStyle("-fx-text-fill: #22c55e;");
            refreshTable(table);
        });

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        VBox root = new VBox(20);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);
        root.setStyle(StyleHelper.MAIN_BG);
        root.getChildren().addAll(box);
        box.getChildren().addAll(titleLabel, table, actions, msgLabel);

        Scene scene = new Scene(root, 700, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshTable(TableView<Dispatch> table) {
        table.setItems(FXCollections.observableArrayList(
                dispatchService.getDispatchesForUser("Destination", currentUser.getId())));
    }
}
