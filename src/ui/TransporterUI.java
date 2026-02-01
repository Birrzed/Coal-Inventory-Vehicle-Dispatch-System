package ui;

import model.Dispatch;
import model.User;
import service.DispatchService;
import service.impl.DispatchServiceImpl;
import service.PaymentService;
import service.impl.PaymentServiceImpl;
import model.Payment;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TransporterUI {
    private User currentUser;
    private DispatchService dispatchService;
    private PaymentService paymentService;

    public TransporterUI(User user) {
        this.currentUser = user;
        this.dispatchService = new DispatchServiceImpl();
        this.paymentService = new PaymentServiceImpl();
    }

    public void start(Stage stage) {
        stage.setTitle("Transporter Dashboard - " + currentUser.getUsername());

        TabPane tabPane = new TabPane();

        // Tab 1: Dispatches
        Tab dispatchTab = new Tab("Assigned Dispatches", createDispatchView(stage));
        dispatchTab.setClosable(false);

        // Tab 2: Payments
        Tab paymentTab = new Tab("My Payments", createPaymentView());
        paymentTab.setClosable(false);

        tabPane.getTabs().addAll(dispatchTab, paymentTab);

        Scene scene = new Scene(tabPane, 700, 550);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDispatchView(Stage stage) {
        Label titleLabel = new Label("Assigned Dispatches");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Dispatch> table = new TableView<>();

        TableColumn<Dispatch, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Dispatch, Double> massCol = new TableColumn<>("Mass (Tons)");
        massCol.setCellValueFactory(new PropertyValueFactory<>("productMass"));

        TableColumn<Dispatch, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, massCol, statusCol);

        Button confirmButton = new Button("Confirm Receipt (Start Transport)");
        confirmButton.setStyle(
                "-fx-background-color: #f1c40f; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10 20; -fx-background-radius: 10;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 10;");

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

            if (!"Pending".equals(selected.getStatus())) {
                msgLabel.setText("Only 'Pending' dispatches can be confirmed.");
                msgLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            dispatchService.confirmDispatch(selected.getId());
            msgLabel.setText("Dispatch Confirmed! In Transit.");
            msgLabel.setStyle("-fx-text-fill: green;");
            refreshTable(table);
        });

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, table, confirmButton, msgLabel, logoutButton);
        root.setStyle("-fx-background-color: #2c3e50;");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        return root;
    }

    private VBox createPaymentView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);

        Label lbl = new Label("My Payments");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: white;");

        TableView<Payment> table = new TableView<>();

        TableColumn<Payment, Integer> dispCol = new TableColumn<>("Dispatch ID");
        dispCol.setCellValueFactory(new PropertyValueFactory<>("dispatchId"));

        TableColumn<Payment, Double> amtCol = new TableColumn<>("Amount (ETB)");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(dispCol, amtCol, statusCol);
        table.setItems(FXCollections.observableArrayList(paymentService.getTransporterPayments(currentUser.getId())));

        Button confirmBtn = new Button("Confirm Receipt of Payment");
        confirmBtn.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 20;");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: white;");

        confirmBtn.setOnAction(e -> {
            Payment selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a payment.");
                return;
            }
            if (!"Paid".equals(selected.getStatus())) {
                msg.setText("Payment is already confirmed.");
                return;
            }

            paymentService.confirmPaymentReceipt(selected.getId());
            msg.setText("Payment of " + selected.getAmount() + " ETB Confirmed as Received!");
            msg.setStyle("-fx-text-fill: green;");
            table.setItems(
                    FXCollections.observableArrayList(paymentService.getTransporterPayments(currentUser.getId())));
        });

        box.getChildren().addAll(lbl, table, confirmBtn, msg);
        box.setStyle("-fx-background-color: #2c3e50;");
        return box;
    }

    private void refreshTable(TableView<Dispatch> table) {
        table.setItems(FXCollections.observableArrayList(
                dispatchService.getDispatchesForUser("Transporter", currentUser.getId())));
    }
}
