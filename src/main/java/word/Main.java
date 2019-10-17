package word;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("word.fxml"));
        primaryStage.setTitle("词频分析器");
        primaryStage.setScene(new Scene(root, 710, 400));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if(Controller.executor!=null){
            Controller.executor.shutdown();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
