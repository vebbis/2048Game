package tyvefortiatte;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TyveFørtiÅtteApp extends Application {

    
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TyveFørtiÅtte.fxml"));
        Parent root = fxmlLoader.load();
        TyveFørtiÅtteController controller = fxmlLoader.getController(); 
        Scene scene = new Scene(root);
        
        //to be able to move the board with KeyCodes
        scene.getRoot().requestFocus();         
        scene.setOnKeyPressed(event -> {controller.handleKeyPress(event.getCode());});

        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
}

