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
import model.Payment;
import service.PaymentService;
import service.impl.PaymentServiceImpl;
import java.sql.Date;
import java.util.List;

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
        tabPane.setStyle("-fx-background-color: transparent;");

        Tab dispatchTab = new Tab("Dispatch Management");
        dispatchTab.setClosable(false);
        dispatchTab.setContent(createDispatchView(stage));

        Tab paymentTab = new Tab("Payment Status");
        paymentTab.setClosable(false);
        paymentTab.setContent(createPaymentView());

        tabPane.getTabs().addAll(dispatchTab, paymentTab);

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle(StyleHelper.MAIN_BG);
        root.getChildren().addAll(tabPane);

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    private VBox createDispatchView(Stage stage) {
        VBox box = new VBox(20);
        box.setPadding(new Insets(30));
        box.setStyle(StyleHelper.GLASS_PANEL);

        Label titleLabel = new Label("Assigned Dispatches");
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

        Button confirmButton = new Button("Confirm Receipt (Start Transport)");
        confirmButton.setStyle(StyleHelper.BUTTON_PRIMARY);

        Button disapproveButton = new Button("Disapprove Dispatch");
        disapproveButton.setStyle(StyleHelper.BUTTON_SECONDARY);

        Button deleteButton = new Button("Delete Dispatch");
        deleteButton.setStyle(StyleHelper.BUTTON_DANGER);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(StyleHelper.BUTTON_SECONDARY);

        actions.getChildren().addAll(confirmButton, disapproveButton, deleteButton, logoutButton);

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

            if (!"Pending".equals(selected.getStatus())) {
                msgLabel.setText("Only 'Pending' dispatches can be confirmed.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            dispatchService.confirmDispatch(selected.getId());
            msgLabel.setText("Dispatch Confirmed! In Transit.");
            msgLabel.setStyle("-fx-text-fill: #22c55e;");
            refreshTable(table);
        });

        disapproveButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msgLabel.setText("Please select a dispatch.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            if (!"Pending".equals(selected.getStatus())) {
                msgLabel.setText("Only 'Pending' dispatches can be disapproved.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            dispatchService.disapproveDispatch(selected.getId());
            msgLabel.setText("Dispatch Disapproved!");
            msgLabel.setStyle("-fx-text-fill: #f59e0b;");
            refreshTable(table);
        });

        deleteButton.setOnAction(e -> {
            Dispatch selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msgLabel.setText("Please select a dispatch.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            if (!"Pending".equals(selected.getStatus())) {
                msgLabel.setText("Only 'Pending' dispatches can be deleted.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            dispatchService.deleteDispatch(selected.getId());
            msgLabel.setText("Dispatch Deleted!");
            msgLabel.setStyle("-fx-text-fill: #ef4444;");
            refreshTable(table);
        });

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        box.getChildren().addAll(titleLabel, table, actions, msgLabel);
        return box;
    }

    private VBox createPaymentView() {
        VBox box = new VBox(20);
        box.setPadding(new Insets(30));
        box.setStyle(StyleHelper.GLASS_PANEL);

        Label titleLabel = new Label("My Payments");
        titleLabel.setStyle(StyleHelper.HEADER_TEXT);

        TableView<Payment> table = new TableView<>();
        table.setStyle(StyleHelper.TABLE_STYLE);

        TableColumn<Payment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Payment, Double> amtCol = new TableColumn<>("Amount (ETB)");
        amtCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        TableColumn<Payment, Date> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));

        TableColumn<Payment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, amtCol, dateCol, statusCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label totalLabel = new Label("Total Earned: 0.00 ETB");
        totalLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #22c55e;");

        Button confirmBtn = new Button("Confirm Payment Received");
        confirmBtn.setStyle(StyleHelper.BUTTON_PRIMARY);

        Label msgLabel = new Label();
        msgLabel.setStyle(StyleHelper.NORMAL_TEXT);

        // Initial Load
        refreshPaymentTable(table, totalLabel);

        confirmBtn.setOnAction(e -> {
            Payment selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msgLabel.setText("Please select a payment.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            if (!"Paid".equals(selected.getStatus())) {
                msgLabel.setText("Payment already confirmed or in other status.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            paymentService.confirmPaymentReceipt(selected.getId());
            msgLabel.setText("Payment Receipt Confirmed!");
            msgLabel.setStyle("-fx-text-fill: #22c55e;");
            refreshPaymentTable(table, totalLabel);
        });

        box.getChildren().addAll(titleLabel, table, totalLabel, confirmBtn, msgLabel);
        return box;
    }

    private void refreshPaymentTable(TableView<Payment> table, Label totalLabel) {
        List<Payment> payments = paymentService.getPaymentsForTransporter(currentUser.getId());
        table.setItems(FXCollections.observableArrayList(payments));
        double total = payments.stream().mapToDouble(Payment::getAmount).sum();
        totalLabel.setText(String.format("Total Earned: %,.2f ETB", total));
    }

    private void refreshTable(TableView<Dispatch> table) {
        table.setItems(FXCollections.observableArrayList(
                dispatchService.getDispatchesForUser("Transporter", currentUser.getId())));
    }
}
