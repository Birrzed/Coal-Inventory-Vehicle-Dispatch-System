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
import javafx.scene.layout.HBox;
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

    public void start(Stage stage) {
        stage.setTitle("Admin Dashboard - Main Office");

        TabPane tabPane = new TabPane();
        tabPane.setStyle("-fx-background-color: transparent;");

        // --- Tab 1: Dispatch Management ---
        Tab dispatchTab = new Tab("Dispatches", createDispatchView(stage));
        dispatchTab.setClosable(false);

        // --- Tab 2: Payment Reports ---
        Tab paymentTab = new Tab("Payments", createPaymentView());
        paymentTab.setClosable(false);

        tabPane.getTabs().addAll(dispatchTab, paymentTab);

        VBox root = new VBox(10);
        root.setStyle(StyleHelper.MAIN_BG);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(tabPane);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDispatchView(Stage stage) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setStyle(StyleHelper.GLASS_PANEL);

        Label lbl = new Label("Dispatch Management");
        lbl.setStyle(StyleHelper.HEADER_TEXT);

        TableView<Dispatch> table = new TableView<>();
        table.setStyle(StyleHelper.TABLE_STYLE);

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

        TableColumn<Dispatch, String> transCol = new TableColumn<>("Transporter");
        transCol.setCellValueFactory(new PropertyValueFactory<>("transporterName"));

        TableColumn<Dispatch, String> destCol = new TableColumn<>("Destination");
        destCol.setCellValueFactory(new PropertyValueFactory<>("destinationName"));

        table.getColumns().addAll(idCol, massCol, dateCol1, dateCol2, transCol, destCol, statusCol);
        table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER_LEFT);

        Button payButton = new Button("Process Payment");
        payButton.setStyle(StyleHelper.BUTTON_PRIMARY);

        Button deleteButton = new Button("Delete Dispatch");
        deleteButton.setStyle(StyleHelper.BUTTON_DANGER);

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle(StyleHelper.BUTTON_SECONDARY);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(StyleHelper.BUTTON_SECONDARY);

        actions.getChildren().addAll(payButton, deleteButton, refreshButton, logoutButton);

        Label msg = new Label();
        msg.setStyle(StyleHelper.NORMAL_TEXT);

        payButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a dispatch.");
                msg.setStyle("-fx-text-fill: #ef4444;");
                return;
            }
            if (!"Delivered".equals(selected.getStatus())) {
                msg.setText("Only 'Delivered' dispatches can be paid.");
                msg.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            paymentService.calculateAndProcessPayment(selected.getId());
            msg.setText("Payment Processed & Status Completed!");
            msg.setStyle("-fx-text-fill: #22c55e;");
            table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches()));
        });

        deleteButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a dispatch to delete.");
                msg.setStyle("-fx-text-fill: #ef4444;");
                return;
            }
            dispatchService.deleteDispatch(selected.getId());
            msg.setText("Dispatch Deleted!");
            msg.setStyle("-fx-text-fill: #22c55e;");
            table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches()));
        });

        refreshButton.setOnAction(
                e -> table.setItems(FXCollections.observableArrayList(dispatchService.getAllDispatches())));

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        box.getChildren().addAll(lbl, table, actions, msg);
        return box;
    }

    private VBox createPaymentView() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(20));
        box.setStyle(StyleHelper.GLASS_PANEL);

        Label lbl = new Label("Processed Payments");
        lbl.setStyle(StyleHelper.HEADER_TEXT);

        TableView<Payment> table = new TableView<>();
        table.setStyle(StyleHelper.TABLE_STYLE);

        TableColumn<Payment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Payment, Integer> dispCol = new TableColumn<>("Dispatch ID");
        dispCol.setCellValueFactory(new PropertyValueFactory<>("dispatchId"));

        TableColumn<Payment, Double> amtCol = new TableColumn<>("Amount (ETB)");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, Date> dateCol = new TableColumn<>("Payment Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        TableColumn<Payment, String> transCol = new TableColumn<>("Transporter");
        transCol.setCellValueFactory(new PropertyValueFactory<>("transporterName"));

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, dispCol, amtCol, dateCol, transCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label totalAmountLabel = new Label("Total Revenue: 0.00 ETB");
        totalAmountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #22c55e;");

        Button refreshButton = new Button("Refresh Data");
        refreshButton.setStyle(StyleHelper.BUTTON_SECONDARY);

        // Initial load
        List<Payment> allPayments = paymentService.getAllPayments();
        table.setItems(FXCollections.observableArrayList(allPayments));
        updateTotalLabel(totalAmountLabel, allPayments);

        refreshButton.setOnAction(e -> {
            List<Payment> updatedPayments = paymentService.getAllPayments();
            table.setItems(FXCollections.observableArrayList(updatedPayments));
            updateTotalLabel(totalAmountLabel, updatedPayments);
        });

        box.getChildren().addAll(lbl, table, totalAmountLabel, refreshButton);
        return box;
    }

    private void updateTotalLabel(Label label, List<Payment> payments) {
        double total = payments.stream().mapToDouble(Payment::getAmount).sum();
        label.setText(String.format("Total Revenue: %,.2f ETB", total));
    }
}
