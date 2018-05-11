package main.client.login;

import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.exceptions.InvalidArgumentException;

public class MainLauncher extends Application {

	private static Stage primaryStageObj;
	private static String host="";
	private static int port=-1;

	@Override
	public void start(Stage primaryStage) throws Exception {
	
		primaryStageObj = primaryStage;
		
		URL fxml = new File("src/main/resources/views/LoginView.fxml").toURI().toURL();
		Parent root = FXMLLoader.load(fxml);  
		
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("Boggle is life");
		
		File img = new File("src/main/resources/images/dice.png");
		primaryStage.getIcons().add(new Image(img.toURI().toString()));
		
		Scene mainScene = new Scene(root,350, 420);
		mainScene.setRoot(root);
		
		primaryStage.setResizable(true);
		primaryStage.setScene(mainScene);
		primaryStage.sizeToScene();
		primaryStage.centerOnScreen();
		
		if(host!="")LoginController.getInstance().setHost(host);	
		if(port!=-1)LoginController.getInstance().setPort(port);	
		
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> Platform.exit());
		
	}


	public static void main(String[] args) {
		try{
		if(args.length==2){
			if(args[0].equals("-port")){
				if(Integer.parseInt(args[1])>0 && Integer.parseInt(args[1])<65535){
				port = Integer.parseInt(args[1]);
				}
				else{
					throw new InvalidArgumentException("Usage:");
				}
			}

			else if(args[0].equals("-serveur")){
				if(args[1] instanceof String){
					host = args[1];
				}
				else{
					throw new InvalidArgumentException("Usage:");
				}
			}

			else{
				throw new InvalidArgumentException("Usage:");
			}
		}

		else if(args.length==4){
			if(args[0].equals("-port")){
				if(Integer.parseInt(args[1])>0 && Integer.parseInt(args[1])<65535){
					port = Integer.parseInt(args[1]);
				}
				else{
					throw new InvalidArgumentException("Usage:");
				}
			}

			if(args[0].equals("-serveur")){
				if(args[1] instanceof String){
					host = args[1];
				}
				else{
					throw new InvalidArgumentException("Usage:");
				}
			}
			
			if(args[2].equals("-port")){
				if(Integer.parseInt(args[3])>0 && Integer.parseInt(args[3])<65535){
					port = Integer.parseInt(args[3]);
				}
				else{
					throw new InvalidArgumentException("Usage:");
				}
			}

			if(args[2].equals("-serveur")){
				if(args[3] instanceof String){
					host = args[3];
				}
				else{
					throw new InvalidArgumentException("Usage:");
				}
			}
		}
		launch(args);
		}
		catch(InvalidArgumentException é){
			System.out.println("Usage : -port <portnumber> -serveur <hostname>");
			System.exit(1);
		}
	}

	public static Stage getPrimaryStage() {
		return primaryStageObj;
	}
}