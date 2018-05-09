package main.client.chatwindow;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.client.login.MainLauncher;
import main.client.util.VoicePlayback;
import main.client.util.VoiceRecorder;
import main.client.util.VoiceUtil;
import main.client.util.Word;
import main.messages.Message;
import main.messages.MessageType;
import main.messages.Status;
import main.messages.User;
import main.messages.bubble.BubbleSpec;
import main.messages.bubble.BubbledLabel;
import main.traynotifications.animations.AnimationType;
import main.traynotifications.notification.TrayNotification;


public class ChatController implements Initializable {

	@FXML private TextArea messageBox;
	@FXML private Label usernameLabel;
	@FXML private Label currentWord;
	@FXML private Label onlineCountLabel;
	@FXML private ListView userList;
	@FXML private ImageView userImageView;
	@FXML private Button recordBtn;
	@FXML ListView chatPane;
	@FXML ListView statusList;
	@FXML BorderPane borderPane;
	@FXML ComboBox statusComboBox;
	@FXML ImageView microphoneImageView;
	@FXML Button letter1,letter2,letter3,letter4,letter5,letter6,letter7,letter8,letter9,letter10,letter11,letter12,letter13,letter14,letter15,letter16;
	@FXML Label foundWords;


	Word word = new Word();

	File imgMicrophoneActive = new File("src/main/resources/images/microphone-active.png");
	File imgMicrophone = new File("src/main/resources/images/microphone.png");

	Image microphoneActiveImage = new Image(imgMicrophoneActive.toURI().toString());
	Image microphoneInactiveImage = new Image(imgMicrophone.toURI().toString());

	private double xOffset;
	private double yOffset;
	Logger logger = LoggerFactory.getLogger(ChatController.class);


	public void sendButtonAction() throws IOException {
		String msg = messageBox.getText();
		if (!messageBox.getText().isEmpty()) {
			try {
				Listener.send(msg);
				messageBox.clear();
			}catch(SocketException e) {
				showErrorDialog("Connection Error", "Your client has been disconnected !");

			}
		}
	}

	public void addLetterAction(ActionEvent event){
		Node node = (Node) event.getSource();
		String pos = (String) node.getUserData();
		Button pressed = (Button) event.getSource();
		char letter = pressed.getText().charAt(0);
		StringBuilder builder = new StringBuilder(word.getLetters().size());
		StringBuilder builderPos = new StringBuilder(word.getTrail().size());
		word.addLetter(letter,pos);  

		for(Character ch: word.getLetters())
		{
			builder.append(ch);
		}

		currentWord.setText(builder.toString());
	}

	public void removeLetterAction() {
		System.out.println("Removing letter");
		word.removeLetter();
		StringBuilder builder = new StringBuilder(word.getLetters().size());	
		for(Character ch: word.getLetters())
		{
			builder.append(ch);
		}	

		currentWord.setText( builder.toString());
	}

	public void resetBoardAction() {
		System.out.println("Reset board");
		word.resetBoard();
		currentWord.setText("");
	}

	public void sendWordAction() throws IOException {
		if(word.getLetters().size()!=0) {
			StringBuilder builder = new StringBuilder(word.getLetters().size());
			StringBuilder builderPos = new StringBuilder(word.getLetters().size());

			for(Character ch: word.getLetters())
			{
				builder.append(ch);
			}

			for(int[] p: word.getTrail())
			{
				builderPos.append(p[0]);
				builderPos.append(p[1]);
			}
			
			if(foundWords.getText()!="")foundWords.setText(foundWords.getText()+","+builder.toString());
			
			else foundWords.setText(builder.toString());
			

			Listener.sendRaw("TROUVE/"+builder.toString()+"/"+builderPos.toString());
			word.resetBoard();
			currentWord.setText("");
		}
	}

	public void recordVoiceMessage() throws IOException {
		if (VoiceUtil.isRecording()) {
			Platform.runLater(() -> {
				microphoneImageView.setImage(microphoneInactiveImage);
			}
					);
			VoiceUtil.setRecording(false);
		} else {
			Platform.runLater(() -> {
				microphoneImageView.setImage(microphoneActiveImage);

			}
					);
			VoiceRecorder.captureAudio();
		}
	}


	public synchronized void addToChat(Message msg) {
		Task<HBox> othersMessages = new Task<HBox>() {
			@Override
			public HBox call() throws Exception {
				File pic = new File("src/main/resources/images/" + msg.getPicture() + ".png");
				Image image = new Image(pic.toURI().toString());
				ImageView profileImage = new ImageView(image);
				profileImage.setFitHeight(32);
				profileImage.setFitWidth(32);
				BubbledLabel bl6 = new BubbledLabel();
				if (msg.getType() == MessageType.VOICE){
					File soundpic = new File("src/main/resources/images/sound.png");
					ImageView imageview = new ImageView(new Image(soundpic.toURI().toString()));
					bl6.setGraphic(imageview);
					bl6.setText("Sent a voice message!");
					VoicePlayback.playAudio(msg.getVoiceMsg());
				}else {
					bl6.setText(msg.getName() + ": " + msg.getMsg());
				}
				bl6.setBackground(new Background(new BackgroundFill(Color.WHITE,null, null)));
				HBox x = new HBox();
				bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
				x.getChildren().addAll(profileImage, bl6);
				logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
				setOnlineLabel(Integer.toString(msg.getOnlineCount()));
				return x;
			}
		};

		othersMessages.setOnSucceeded(event -> {
			chatPane.getItems().add(othersMessages.getValue());
		});

		Task<HBox> yourMessages = new Task<HBox>() {
			@Override
			public HBox call() throws Exception {
				Image image = userImageView.getImage();
				ImageView profileImage = new ImageView(image);
				profileImage.setFitHeight(32);
				profileImage.setFitWidth(32);

				BubbledLabel bl6 = new BubbledLabel();
				if (msg.getType() == MessageType.VOICE){
					File soundpic = new File("src/main/resources/images/sound.png");
					bl6.setGraphic(new ImageView(new Image(soundpic.toURI().toString())));
					bl6.setText("Sent a voice message!");
					VoicePlayback.playAudio(msg.getVoiceMsg());
				}else {
					bl6.setText(msg.getMsg());
				}
				bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
						null, null)));
				HBox x = new HBox();
				x.setMaxWidth(chatPane.getWidth() - 20);
				x.setAlignment(Pos.TOP_RIGHT);
				bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
				x.getChildren().addAll(bl6, profileImage);

				setOnlineLabel(Integer.toString(msg.getOnlineCount()));
				return x;
			}
		};
		yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));

		if (msg.getName().equals(usernameLabel.getText())) {
			Thread t2 = new Thread(yourMessages);
			t2.setDaemon(true);
			t2.start();
		} else {
			Thread t = new Thread(othersMessages);
			t.setDaemon(true);
			t.start();
		}
	}
	public void setUsernameLabel(String username) {
		this.usernameLabel.setText(username);
	}

	public void setImageLabel() throws IOException {
		//this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/Dominic.png").toString()));
	}

	public void setOnlineLabel(String usercount) {
		Platform.runLater(() -> onlineCountLabel.setText(usercount));
	}

	public void setUserList(Message msg) {
		logger.info("setUserList() method Enter");
		Platform.runLater(() -> {
			ObservableList<User> users = FXCollections.observableList(msg.getUsers());
			userList.setItems(users);
			userList.setCellFactory(new CellRenderer());
			setOnlineLabel(String.valueOf(msg.getUserlist().size()));
		});
		logger.info("setUserList() method Exit");
	}

	/* Displays Notification when a user joins */
	public void newUserNotification(Message msg) {
		Platform.runLater(() -> {
			File img = new File("src/main/resources/images/" + msg.getPicture().toLowerCase() +".png");
			Image profileImg = new Image(img.toURI().toString(),50,50,false,false);
			TrayNotification tray = new TrayNotification();
			tray.setTitle("A new user has joined!");
			tray.setMessage(msg.getName() + " has joined your Boggle room");
			tray.setRectangleFill(Paint.valueOf("#3498db"));
			tray.setAnimationType(AnimationType.POPUP);
			tray.setImage(profileImg);
			tray.showAndDismiss(Duration.seconds(5));
			try {
				File hitFile = new File("src/main/resources/sounds/notification.wav");
				Media hit = new Media(hitFile.toURI().toString());
				MediaPlayer mediaPlayer = new MediaPlayer(hit);
				mediaPlayer.play();
			} catch (Exception e) {
				e.printStackTrace();
			}

		});
	}

	public void sendMethod(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			sendButtonAction();
		}
	}

	@FXML
	public void closeApplication() {
		System.out.println("SORT/"+usernameLabel.getText()+"/");
		Platform.exit();
		System.exit(0);
	}

	/* Method to display server messages */
	public synchronized void addAsServer(Message msg) {
		Task<HBox> task = new Task<HBox>() {
			@Override
			public HBox call() throws Exception {
				BubbledLabel bl6 = new BubbledLabel();
				bl6.setText(msg.getMsg());
				bl6.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,
						null, null)));
				HBox x = new HBox();
				bl6.setBubbleSpec(BubbleSpec.FACE_BOTTOM);
				x.setAlignment(Pos.CENTER);
				x.getChildren().addAll(bl6);
				return x;
			}
		};
		task.setOnSucceeded(event -> {
			chatPane.getItems().add(task.getValue());
		});

		Thread t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			setImageLabel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/* Drag and Drop */
		borderPane.setOnMousePressed(event -> {
			xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
			yOffset = MainLauncher.getPrimaryStage().getY() - event.getScreenY();
			borderPane.setCursor(Cursor.CLOSED_HAND);
		});

		borderPane.setOnMouseDragged(event -> {
			MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
			MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

		});

		borderPane.setOnMouseReleased(event -> {
			borderPane.setCursor(Cursor.DEFAULT);
		});

		statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					Listener.sendStatusUpdate(Status.valueOf(newValue.toUpperCase()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		/* Added to prevent the enter from adding a new line to inputMessageBox */
		messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
			if (ke.getCode().equals(KeyCode.ENTER)) {
				try {
					sendButtonAction();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ke.consume();
			}
		});

	}

	public void setImageLabel(String selectedPicture) {
		File userImage;
		        switch (selectedPicture) {
		            case "Dominic":
		            	userImage = new File("src/main/ressources/images/Dominic.png");
		            	this.userImageView.setImage(new Image(userImage.toURI().toString()));
		                break;
		            case "Sarah":
		            	userImage = new File("src/main/ressources/images/sarah.png");
		            	this.userImageView.setImage(new Image(userImage.toURI().toString()));
		                break;
		            case "Default":
		            	userImage = new File("src/main/ressources/images/default.png");
		            	this.userImageView.setImage(new Image(userImage.toURI().toString()));
		                break;
		        }
	}

	public void logoutScene() {
		Platform.runLater(() -> {
			URL fxmll = null;
			try {
				fxmll = new File("src/main/resources/views/LoginView.fxml").toURI().toURL();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}    

			FXMLLoader fmxlLoader = new FXMLLoader(fxmll);
			Parent window = null;

			try {
				window = (Pane) fmxlLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Stage stage = MainLauncher.getPrimaryStage();
			Scene scene = new Scene(window);
			stage.setMaxWidth(350);
			stage.setMaxHeight(420);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.centerOnScreen();
		});
	}

	public void setMatrix(Message message) {
		message.getMsg();
	}

	public void showErrorDialog(String message, String content) {
		Platform.runLater(()-> {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Warning!");
			alert.setHeaderText(message);
			alert.setContentText(content);
			alert.showAndWait();
		});

	}
}