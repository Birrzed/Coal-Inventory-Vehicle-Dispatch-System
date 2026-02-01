package ui;

import model.Dispatch;
import model.Payment;
import model.User;
import service.DispatchService;
import service.PaymentService;
import service.impl.DispatchServiceImpl;
import service.impl.PaymentServiceImpl;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Date;
import java.util.List;

public class AdminUI {
    private User currentUser;
    private DispatchService dispatchService;
    private PaymentService paymentService;

    public AdminUI(User user) {
        this.currentUser = user;
        this.dispatchService = new DispatchServiceImpl();
        this.paymentService = new PaymentServiceImpl();
    }

    // Initialize the main dashboard with TabPane navigation
    public void start(Stage stage) {
        stage.setTitle("Admin Dashboard - Main Office");

        TabPane tabPane = new TabPane();

        // --- Tab 1: Dispatch Management ---
        Tab dispatchTab = new Tab("Dispatches", createDispatchView(stage));
        dispatchTab.setClosable(false);

        // --- Tab 2: Payment Reports ---
        Tab paymentTab = new Tab("Payments", createPaymentView());
        paymentTab.setClosable(false);

        tabPane.getTabs().addAll(dispatchTab, paymentTab);

        VBox root = new VBox(10);
        root.getChildren().addAll(tabPane);
        root.setStyle("-fx-background-color: #2c3e50;");
        tabPane.setStyle("-fx-background-color: transparent;");
        root.setStyle("-fx-background-color: #2c3e50;");
        tabPane.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDispatchView(Stage stage) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label lbl = new Label("All Dispatches");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 10;");

        TableView<Dispatch> table = new TableView<>();

        TableColumn<Dispatch, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Dispatch, Double> massCol = new TableColumn<>("Mass");
        massCol.setCellValueFactory(new PropertyValueFactory<>("productMass"));

        TableColumn<Dispatch, Date> dateCol1 = new TableColumn<>("Dispatch Date");
        dateCol1.setCellValueFactory(new PropertyValueFactory<>("dispatchDate"));

        TableColumn<Dispatch, Date> dateCol2 = new TableColumn<>("Arrival Date");
        dateCol2.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));

        TableColumn<Dispatch, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, massCol, dateCol1, dateCol2, statusCol);
        table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches()));

        Button payButton = new Button("Process Payment");
        payButton.setStyle(
                "-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20; -fx-background-radius: 5;");

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 5;");

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(
                "-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 5;");

        Label msg = new Label();
        msg.setStyle("-fx-text-fill: white;");

        payButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a dispatch.");
                return;
            }
            if (!"Delivered".equals(selected.getStatus())) {
                msg.setText("Only 'Delivered' dispatches can be paid.");
                return;
            }

            paymentService.calculateAndProcessPayment(selected.getId());
            msg.setText("Payment Processed! Check the 'Payments' tab.");
            msg.setStyle("-fx-text-fill: green;");

            // Refresh Both Tables
            table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches()));
        });

        refreshButton.setOnAction(
                e -> table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches())));

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        box.getChildren().addAll(lbl, table, payButton, refreshButton, msg, logoutButton);
        return box;
    }

    private VBox createPaymentView() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));

        Label lbl = new Label("Processed Payments");
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: white;");

        TableView<Payment> table = new TableView<>();

        TableColumn<Payment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Payment, Integer> dispCol = new TableColumn<>("Dispatch ID");
        dispCol.setCellValueFactory(new PropertyValueFactory<>("dispatchId"));

        TableColumn<Payment, Double> amtCol = new TableColumn<>("Amount (ETB)");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, Date> dateCol = new TableColumn<>("Payment Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, dispCol, amtCol, dateCol, statusCol);

        Label totalLabel = new Label();
        totalLabel.setStyle(
                "-fx-font-weight: bold; -fx-text-fill: #2ecc71; -fx-font-size: 18px; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.3); -fx-background-radius: 10;");

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 8 20; -fx-background-radius: 5;");

        // Helper to update total
        Runnable updateTotal = () -> {
            List<Payment> payments = paymentService.getAllPayments();
            table.setItems(FXCollections.observableArrayList(payments));
            double total = payments.stream().mapToDouble(Payment::getAmount).sum();
            totalLabel.setText("Total Paid Amount: " + String.format("%.2f", total) + " ETB");
        };

        refreshButton.setOnAction(e -> updateTotal.run());

        // Initial load
        updateTotal.run();

        box.getChildren().addAll(lbl, table, totalLabel, refreshButton);
        return box;
    }
}
