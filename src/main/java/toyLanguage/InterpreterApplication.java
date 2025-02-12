package toyLanguage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import toyLanguage.model.statements.IStatement;

import java.io.IOException;

public class InterpreterApplication extends Application {

    protected IStatement statement;

    public static void main(String[] args) {
        launch(args);
    }

    public InterpreterApplication(IStatement statement) {
        this.statement = statement;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(SelectApplication.class.getResource("select-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(SelectApplication.class.getResource("interpreter-view.fxml"));

        InterpreterController interpreterController = new InterpreterController();
        interpreterController.setStatement(statement);
        fxmlLoader.setController(interpreterController);

        Scene scene = new Scene(fxmlLoader.load());

        primaryStage.setTitle("Interpreter");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
