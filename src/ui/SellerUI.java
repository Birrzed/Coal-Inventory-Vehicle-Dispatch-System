package ui;

import dao.UserDAO;
import model.User;
import model.Dispatch;
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
import java.util.Date;
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

        Label titleLabel = new Label("Create Dispatch Record");
        titleLabel.setStyle(StyleHelper.HEADER_TEXT);

        // Form Fields
        Label massLabel = new Label("Product Mass (Tons)");
        massLabel.setStyle(StyleHelper.LABEL_STYLE);
        TextField massField = new TextField();
        massField.setPromptText("Enter mass...");
        massField.setStyle(StyleHelper.TEXT_FIELD);

        Label transLabel = new Label("Select Transporter");
        transLabel.setStyle(StyleHelper.LABEL_STYLE);
        ComboBox<String> transCombo = new ComboBox<>();
        transCombo.setPromptText("Choose transporter...");
        transCombo.setStyle(StyleHelper.TEXT_FIELD);
        transCombo.setMaxWidth(Double.MAX_VALUE);

        List<User> transporters = userDAO.getUsersByRole("Transporter");
        for (User u : transporters) {
            transCombo.getItems().add(u.getId() + ": " + u.getUsername());
        }

        Label destLabel = new Label("Select Destination");
        destLabel.setStyle(StyleHelper.LABEL_STYLE);
        ComboBox<String> destCombo = new ComboBox<>();
        destCombo.setPromptText("Choose destination...");
        destCombo.setStyle(StyleHelper.TEXT_FIELD);
        destCombo.setMaxWidth(Double.MAX_VALUE);

        List<User> destinations = userDAO.getUsersByRole("Destination");
        for (User u : destinations) {
            destCombo.getItems().add(u.getId() + ": " + u.getUsername());
        }

        Button submitButton = new Button("Submit Dispatch");
        submitButton.setStyle(StyleHelper.BUTTON_PRIMARY);
        submitButton.setMaxWidth(Double.MAX_VALUE);

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle(StyleHelper.BUTTON_SECONDARY);
        logoutButton.setMaxWidth(Double.MAX_VALUE);

        Label msgLabel = new Label();
        msgLabel.setStyle(StyleHelper.NORMAL_TEXT);

        VBox formCard = new VBox(15);
        formCard.setStyle(StyleHelper.GLASS_PANEL);
        formCard.setPrefWidth(300);
        formCard.getChildren().addAll(titleLabel, massLabel, massField, transLabel, transCombo, destLabel, destCombo,
                submitButton, logoutButton, msgLabel);

        // Sidebar / Form on the left, Table on the right
        VBox leftPane = new VBox(20);
        leftPane.setPadding(new Insets(20));
        leftPane.getChildren().add(formCard);

        VBox rightPane = new VBox(20);
        rightPane.setPadding(new Insets(20));
        rightPane.setStyle(StyleHelper.GLASS_PANEL);

        Label tableTitle = new Label("My Dispatches");
        tableTitle.setStyle(StyleHelper.HEADER_TEXT);

        TableView<Dispatch> table = new TableView<>();
        table.setStyle(StyleHelper.TABLE_STYLE);

        TableColumn<Dispatch, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Dispatch, Double> mCol = new TableColumn<>("Mass");
        mCol.setCellValueFactory(new PropertyValueFactory<>("productMass"));
        TableColumn<Dispatch, String> sCol = new TableColumn<>("Status");
        sCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, mCol, sCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        refreshTable(table);

        rightPane.getChildren().addAll(tableTitle, table);

        HBox mainLayout = new HBox(10);
        mainLayout.setStyle(StyleHelper.MAIN_BG);
        mainLayout.getChildren().addAll(leftPane, rightPane);
        HBox.setHgrow(rightPane, javafx.scene.layout.Priority.ALWAYS);

        submitButton.setOnAction(e -> {
            try {
                String massText = massField.getText();
                String transSelection = transCombo.getValue();
                String destSelection = destCombo.getValue();

                if (massText.isEmpty() || transSelection == null || destSelection == null) {
                    msgLabel.setText("Please fill all fields.");
                    msgLabel.setStyle("-fx-text-fill: #ef4444;");
                    return;
                }

                double mass = Double.parseDouble(massText);
                int transId = Integer.parseInt(transSelection.split(":")[0]);
                int destId = Integer.parseInt(destSelection.split(":")[0]);

                dispatchService.createDispatch(mass, currentUser.getId(), transId, destId);
                msgLabel.setText("Dispatch Created Successfully!");
                msgLabel.setStyle("-fx-text-fill: #22c55e;");
                refreshTable(table);

                // Clear fields
                massField.clear();
                transCombo.getSelectionModel().clearSelection();
                destCombo.getSelectionModel().clearSelection();

            } catch (NumberFormatException ex) {
                msgLabel.setText("Invalid mass value.");
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
            } catch (Exception ex) {
                msgLabel.setText("Error: " + ex.getMessage());
                msgLabel.setStyle("-fx-text-fill: #ef4444;");
                ex.printStackTrace();
            }
        });

        logoutButton.setOnAction(e -> new LoginUI().start(stage));

        Scene scene = new Scene(mainLayout, 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    private void refreshTable(TableView<Dispatch> table) {
        table.setItems(FXCollections.observableArrayList(
                dispatchService.getDispatchesForUser("Seller", currentUser.getId())));
    }
}
