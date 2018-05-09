package main.client.login;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainLauncher extends Application {

    private static Stage primaryStageObj;

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
    	VBox vbox = new VBox();
		vbox.setPadding(new Insets(10));
		vbox.setSpacing(8);

		Text title = new Text("Data");
		title.setFont(Font.font("SansSerif Regular", FontWeight.BOLD, 14));
		vbox.getChildren().add(title);
    	
        primaryStageObj = primaryStage;
        URL fxml = new File("src/main/resources/views/LoginView.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(fxml);
        
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Boggle is life");
        File img = new File("src/main/resources/images/plug.png");
        primaryStage.getIcons().add(new Image(img.toURI().toString()));
        Scene mainScene = new Scene(root, 1350, 1450);
        mainScene.setRoot(root);
        
        primaryStage.setResizable(true);
        primaryStage.setScene(mainScene);
        primaryStage.sizeToScene();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }
}