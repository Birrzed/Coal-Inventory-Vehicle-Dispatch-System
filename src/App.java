import javafx.application.Application;
import javafx.stage.Stage;
import ui.LoginUI;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        new LoginUI().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
