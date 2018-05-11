package main.client.chatwindow;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.client.login.MainLauncher;
import main.client.util.VoiceRecorder;
import main.client.util.VoiceUtil;
import main.client.util.Word;
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
	@FXML private ListView<User> userList;
	@FXML private ImageView userImageView;
	@FXML private Button recordBtn;
	@FXML private ListView<HBox> chatPane;
	@FXML private ListView<?> statusList;
	@FXML private BorderPane borderPane;
	@FXML private ComboBox statusComboBox;
	@FXML private ImageView microphoneImageView;
	@FXML private Button letter1, letter2,letter3,letter4,letter5,letter6,letter7,letter8,letter9,letter10,letter11,letter12,letter13,letter14,letter15,letter16;
	@FXML private Label foundWords;
	@FXML private Label foundWordsfinal;
	@FXML private Label foundInvWords;
	@FXML private VBox gamePane;
	@FXML private VBox resultPane;
	@FXML private VBox sessionPane;
	@FXML private VBox endPane;
	@FXML private VBox fxscores;
	@FXML private VBox fxscoresfinal;

	private int onlineCpt;
	private int currRound;
	private ArrayList<User> users;
	Word word = new Word();
	private String lastWord;

	private File imgMicrophoneActive = new File("src/main/resources/images/microphone-active.png");
	private File imgMicrophone = new File("src/main/resources/images/microphone.png");

	private Image microphoneActiveImage = new Image(imgMicrophoneActive.toURI().toString());
	private Image microphoneInactiveImage = new Image(imgMicrophone.toURI().toString());

	private double xOffset;
	private double yOffset;
	Logger logger = LoggerFactory.getLogger(ChatController.class);

	public ArrayList<User> getUsersList() {
		return this.users;
	}

	public void sendButtonAction() throws IOException {
		String msg = messageBox.getText();
		if (!messageBox.getText().isEmpty()) {

			if(msg.charAt(0)=='/' && msg.charAt(1)=='w') {
				String[] infos = msg.split(" ");
				String receiver = infos[1];
				String message="";
				for(int i = 2 ; i < infos.length;i++) {
					message+=infos[i]+" ";
				}
				System.out.println("client : PENVOI/"+receiver+"/"+message);
				try {
					Listener.sendRaw("PENVOI/"+receiver+"/"+message+"/\n");
					messageBox.clear();
					addPrivateMessage(message, receiver, usernameLabel.getText());
				}catch(SocketException e) {
					showErrorDialog("Connection Error", "Your client has been disconnected !");

				}
			}

			else {
				try {
					Listener.sendRaw("ENVOI/"+msg+"/\n");
					messageBox.clear();
					addUserMessage(msg, usernameLabel.getText());
				}catch(SocketException e) {
					showErrorDialog("Connection Error", "Your client has been disconnected !");

				}
			}
		}
	}

	public void addLetterAction(ActionEvent event){
		Node node = (Node) event.getSource();
		String pos = (String) node.getUserData();
		Button pressed = (Button) event.getSource();

		if(pressed.getText()!=""){
			char letter = pressed.getText().charAt(0);
			StringBuilder builder = new StringBuilder(word.getLetters().size());
			word.addLetter(letter,pos);

			for(Character ch: word.getLetters())
			{
				builder.append(ch);
			}

			currentWord.setText(builder.toString());
		}
	}

	public void removeLetterAction() {
		//	System.out.println("Removing letter");
		word.removeLetter();
		StringBuilder builder = new StringBuilder(word.getLetters().size());
		for(Character ch: word.getLetters())
		{
			builder.append(ch);
		}

		currentWord.setText( builder.toString());
	}

	public void resetBoardAction() {
		word.resetBoard();
		Platform.runLater(() -> currentWord.setText(""));
	}

	public void sendWordAction() throws IOException {
		if(word.getLetters().size()!=0) {
			StringBuilder builder = new StringBuilder(word.getLetters().size());
			StringBuilder builderPos = new StringBuilder(word.getLetters().size());

			for(Character ch: word.getLetters())
			{
				builder.append(ch);
			}

			for(String p: word.getTrail())
			{
				builderPos.append(p);
			}

			lastWord = builder.toString();
			Listener.sendRaw("TROUVE/"+builder.toString()+"/"+builderPos.toString());
			word.resetBoard();
			currentWord.setText("");
		}
	}

	public void addValidWord(String word) {
		Platform.runLater(() -> {
		if(foundWords.getText()!="")foundWords.setText(foundWords.getText()+","+word);
		else foundWords.setText(word);
		});
	}

	public void addInvalidWord(String res) {
		Platform.runLater(() -> {
		String r;
		if(res.charAt(0)=='P') r="invalid position";
		else if(res.charAt(0)=='D') r="inexsitant word";
		else r="unknow reason";
		if(foundInvWords.getText()!="")foundInvWords.setText(foundInvWords.getText()+","+lastWord+" ("+r+")");
		else foundInvWords.setText(lastWord+" ("+r+")");
		});
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


	public void resetWords() {
		Platform.runLater(() -> foundWords.setText(""));
		Platform.runLater(() -> foundInvWords.setText(""));
		Platform.runLater(() -> currentWord.setText(""));
	}

	public void resetRound() {
		this.currRound=1;
	}


	public void addUserMessage(String msg, String user) {
		Task<HBox> othersMessages = new Task<HBox>() {
			@Override
			public HBox call() throws Exception {
				//	File pic = new File("src/main/resources/images/" + msg.getPicture() + ".png");
				//	Image image = new Image(pic.toURI().toString());
				//	ImageView profileImage = new ImageView(image);
				//	profileImage.setFitHeight(32);
				//	profileImage.setFitWidth(32);
				File pic = new File("src/main/resources/images/default.png");
				Image image = new Image(pic.toURI().toString());
				ImageView profileImage = new ImageView(image);
				profileImage.setFitHeight(32);
				profileImage.setFitWidth(32);
				BubbledLabel bl6 = new BubbledLabel();
				//				if (msg.getType() == MessageType.VOICE){
				//					File soundpic = new File("src/main/resources/images/sound.png");
				//					ImageView imageview = new ImageView(new Image(soundpic.toURI().toString()));
				//					bl6.setGraphic(imageview);
				//					bl6.setText("Sent a voice message!");
				//					VoicePlayback.playAudio(msg.getVoiceMsg());
				//				}else {
				bl6.setText(user + ": " + msg);
				//				}
				bl6.setBackground(new Background(new BackgroundFill(Color.GREY,null, null)));
				HBox x = new HBox();
				bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
				//x.getChildren().add(bl6);
				x.getChildren().addAll(profileImage, bl6);
				//logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
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
				//				if (msg.getType() == MessageType.VOICE){
				//					File soundpic = new File("src/main/resources/images/sound.png");
				//					bl6.setGraphic(new ImageView(new Image(soundpic.toURI().toString())));
				//					bl6.setText("Sent a voice message!");
				//					VoicePlayback.playAudio(msg.getVoiceMsg());
				//				}else {
				bl6.setText(msg);
				//}
				bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
						null, null)));
				HBox x = new HBox();
				x.setMaxWidth(chatPane.getWidth() - 20);
				x.setAlignment(Pos.TOP_RIGHT);
				bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
				//				x.getChildren().add(bl6);
				x.getChildren().addAll(bl6, profileImage);

				return x;
			}
		};
		yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));

		if (user.equals(usernameLabel.getText())) {
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
		Platform.runLater(() -> usernameLabel.setText(username));
	}

	public void setImageLabel() throws IOException {
		File img = new File("src/main/resources/images/Default.png");
		Platform.runLater(() -> userImageView.setImage(new Image(img.toURI().toString())));

	}

	public void setOnlineLabel(String usercount) {
		Platform.runLater(() -> onlineCountLabel.setText(usercount));
	}

	public void setUserListRaw(String[] usrs, int[] scores) {
		logger.info("setUserListRaw() method Enter");
		Platform.runLater(() -> {
			users = new ArrayList<User>();
			for(int i = 0 ; i < usrs.length; i++) {
				User usr = new User();
				usr.setName(usrs[i]);
				usr.setScore(scores[i]);
				usr.setPicture("Default");
				usr.setStatus(Status.ONLINE);
				users.add(usr);
			}

			ObservableList<User> userz = FXCollections.observableList(users);
			userList.setItems(userz);
			userList.setCellFactory(new CellRenderer());
			setNbUser(usrs.length);
			setOnlineLabel(String.valueOf(onlineCpt));
			logger.info("setUserListRaw() method Exit");
		});

	}

	public void updateUserStatus(String name, String status) {
		Platform.runLater(() -> {

			for (User user : users) {
				if(user.getName()=="name") {
					switch(status) {

					case "ONLINE":
						user.setStatus(Status.ONLINE);
						break;

					case "AWAY":
						user.setStatus(Status.AWAY);
						break;

					case "BUSY":
						user.setStatus(Status.BUSY);
						break;

					default:
						break;
					}
				}
			}

			ObservableList<User> userz = FXCollections.observableList(users);
			userList.setItems(userz);
			userList.setCellFactory(new CellRenderer());


		});
	}

	public void addUserToList(String s) {
		Platform.runLater(() -> {
			User newbie = new User();
			newbie.setName(s);
			newbie.setScore(0);
			newbie.setStatus(Status.ONLINE);
			newbie.setPicture("Default");

			users.add(newbie);
			ObservableList<User> userz = FXCollections.observableList(users);
			userList.setItems(userz);
			userList.setCellFactory(new CellRenderer());

			setOnlineLabel(String.valueOf(users.size()));

		});
	}

	public void removeUserFromList(String s) {
		Platform.runLater(() -> {

			users = (ArrayList<User>) users.stream().filter(pulse -> pulse.getName().equals(s)).collect(Collectors.toList());
			ObservableList<User> userz = FXCollections.observableList(users);
			userList.setItems(userz);
			userList.setCellFactory(new CellRenderer());

			setOnlineLabel(String.valueOf(users.size()));

		});
	}



	public void newUserEntryNotification(String username) {
		Platform.runLater(() -> {
			//	File img = new File("src/main/resources/images/" + msg.getPicture().toLowerCase() +".png");
			File img = new File("src/main/resources/images/default.png");
			Image profileImg = new Image(img.toURI().toString(),50,50,false,false);
			TrayNotification tray = new TrayNotification();
			tray.setTitle("A new user has joined!");
			tray.setMessage(username + " has joined your Boggle room");
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

	public void userDisconnectedNotification(String username) {
		Platform.runLater(() -> {
			//	File img = new File("src/main/resources/images/" + msg.getPicture().toLowerCase() +".png");
			File img = new File("src/main/resources/images/default.png");
			Image profileImg = new Image(img.toURI().toString(),50,50,false,false);
			TrayNotification tray = new TrayNotification();
			tray.setTitle("A user left");
			tray.setMessage(username + " has left your Boggle room");
			tray.setRectangleFill(Paint.valueOf("#ffcccc"));
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
		try {
			Listener.sendRaw("SORT/"+Listener.username+"/\n");
		} catch (IOException e) {
			showErrorDialog("Connection Lost", "Connection with server has been interrupted.");
			e.printStackTrace();
		}
		Platform.exit();
		System.exit(0);
	}

	/* Method to display server messages */
	public void addAsServer(String msg) {
		Task<HBox> task = new Task<HBox>() {
			@Override
			public HBox call() throws Exception {
				BubbledLabel bl6 = new BubbledLabel();
				bl6.setText(msg);
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

	public void addPrivateMessage(String msg, String receiver, String sender) {
		Task<HBox> othersMessages = new Task<HBox>() {
			@Override
			public HBox call() throws Exception {
				//	File pic = new File("src/main/resources/images/" + msg.getPicture() + ".png");
				//	Image image = new Image(pic.toURI().toString());
				//	ImageView profileImage = new ImageView(image);
				//	profileImage.setFitHeight(32);
				//	profileImage.setFitWidth(32);
				File pic = new File("src/main/resources/images/default.png");
				Image image = new Image(pic.toURI().toString());
				ImageView profileImage = new ImageView(image);
				profileImage.setFitHeight(32);
				profileImage.setFitWidth(32);
				BubbledLabel bl6 = new BubbledLabel();
				//				if (msg.getType() == MessageType.VOICE){
				//					File soundpic = new File("src/main/resources/images/sound.png");
				//					ImageView imageview = new ImageView(new Image(soundpic.toURI().toString()));
				//					bl6.setGraphic(imageview);
				//					bl6.setText("Sent a voice message!");
				//					VoicePlayback.playAudio(msg.getVoiceMsg());
				//				}else {
				bl6.setText(sender + ": " + msg);
				//				}
				bl6.setBackground(new Background(new BackgroundFill(Color.HOTPINK,null, null)));
				HBox x = new HBox();
				bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
				//x.getChildren().add(bl6);
				x.getChildren().addAll(profileImage, bl6);
				//logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
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
				//				if (msg.getType() == MessageType.VOICE){
				//					File soundpic = new File("src/main/resources/images/sound.png");
				//					bl6.setGraphic(new ImageView(new Image(soundpic.toURI().toString())));
				//					bl6.setText("Sent a voice message!");
				//					VoicePlayback.playAudio(msg.getVoiceMsg());
				//				}else {
				bl6.setText("to "+receiver+ ":" + msg);
				//}
				bl6.setBackground(new Background(new BackgroundFill(Color.BLUEVIOLET,
						null, null)));
				HBox x = new HBox();
				x.setMaxWidth(chatPane.getWidth() - 20);
				x.setAlignment(Pos.TOP_RIGHT);
				bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
				//				x.getChildren().add(bl6);
				x.getChildren().addAll(bl6, profileImage);

				return x;
			}
		};
		yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));

		if (sender.equals(usernameLabel.getText())) {
			Thread t2 = new Thread(yourMessages);
			t2.setDaemon(true);
			t2.start();
		} else {
			Thread t = new Thread(othersMessages);
			t.setDaemon(true);
			t.start();
		}
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
			userImage = new File("src/main/resources/images/Dominic.png");
			this.userImageView.setImage(new Image(userImage.toURI().toString()));
			break;
		case "Sarah":
			userImage = new File("src/main/resources/images/sarah.png");
			this.userImageView.setImage(new Image(userImage.toURI().toString()));
			break;
		case "Default":
			userImage = new File("src/main/resources/images/default.png");
			//System.out.println(userImage.getAbsolutePath());
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

	public void setMatrix(String message) {
		Platform.runLater(() -> {

			initBtn(letter1,String.valueOf(message.charAt(0)));
			initBtn(letter2,String.valueOf(message.charAt(1)));
			initBtn(letter3,String.valueOf(message.charAt(2)));
			initBtn(letter4,String.valueOf(message.charAt(3)));
			initBtn(letter5,String.valueOf(message.charAt(4)));
			initBtn(letter6,String.valueOf(message.charAt(5)));
			initBtn(letter7,String.valueOf(message.charAt(6)));
			initBtn(letter8,String.valueOf(message.charAt(7)));
			initBtn(letter9,String.valueOf(message.charAt(8)));
			initBtn(letter10,String.valueOf(message.charAt(9)));
			initBtn(letter11,String.valueOf(message.charAt(10)));
			initBtn(letter12,String.valueOf(message.charAt(11)));
			initBtn(letter13,String.valueOf(message.charAt(12)));
			initBtn(letter14,String.valueOf(message.charAt(13)));
			initBtn(letter15,String.valueOf(message.charAt(14)));
			initBtn(letter16,String.valueOf(message.charAt(15)));

		});
	}

	public void displayMatrix(boolean b) {
		Platform.runLater(() -> {

			letter1.setVisible(b);
			letter2.setVisible(b);
			letter3.setVisible(b);
			letter4.setVisible(b);
			letter5.setVisible(b);
			letter6.setVisible(b);
			letter7.setVisible(b);
			letter8.setVisible(b);
			letter9.setVisible(b);
			letter10.setVisible(b);
			letter11.setVisible(b);
			letter12.setVisible(b);
			letter13.setVisible(b);
			letter14.setVisible(b);
			letter15.setVisible(b);
			letter16.setVisible(b);

		});
	}

	public void initBtn(Button btn , String txt) {

		int randomNum = ThreadLocalRandom.current().nextInt(0, 3 + 1);
		btn.setText(txt);

		switch(randomNum) {
		case 0:
			btn.setRotate(0);
			break;
		case 1:
			btn.setRotate(90);
			break;
		case 2:
			btn.setRotate(180);
			break;
		case 3:
			btn.setRotate(270);
			break;
		}

		//btn.setVisible(false);
	}

	public void setNbUser(int n){
		onlineCpt = n;
	}

	public void displaySession() {
		Platform.runLater(() -> {
			gamePane.setVisible(false);
			gamePane.setManaged(false);
			resultPane.setVisible(false);
			resultPane.setManaged(false);
			endPane.setVisible(false);
			endPane.setManaged(false);
			sessionPane.setVisible(true);
			sessionPane.setManaged(true);
			resize();
		});
	}

	public void displayEnd(String res) {
		Platform.runLater(() -> {

			String[] infos = res.split("\\*");
			int max=-1;
			String winner="";

			HBox winnerh = new HBox();
			winnerh.setAlignment(Pos.CENTER);
			Label win = new Label("THE WINNER IS ");
			win.setId("subtitle");
			Label winid = new Label();
			winid.setId("subtitle");
			winnerh.getChildren().addAll(win,winid);

			fxscoresfinal.getChildren().clear();
			fxscoresfinal.getChildren().add(winnerh);

			for(int i=0;i<infos.length-1;i=i+2) {
				if(Integer.parseInt(infos[i+1])>max) {
					winner = infos[i];
					max = Integer.parseInt(infos[i+1]);
				}
				if(infos[i].equals(usernameLabel.getText())) {
					Integer.parseInt(infos[i+1]);
					Label sc = new Label("YOUR SCORE  : " +infos[i+1]);
					sc.setId("data");
					fxscoresfinal.getChildren().add(sc);
				}
			}

			winid.setText(winner +" ("+max+" pts)");

			for(int i=0;i<infos.length-1;i=i+2) {
				if(infos[i].equals(usernameLabel.getText())) {

				}
				else {
					Label pl = new Label(infos[i] +" : " +infos[i+1]);
					pl.setId("data");
					fxscoresfinal.getChildren().add(pl);
				}
			}

			gamePane.setVisible(false);
			gamePane.setManaged(false);
			resultPane.setVisible(false);
			resultPane.setManaged(false);
			sessionPane.setVisible(false);
			sessionPane.setManaged(false);
			endPane.setVisible(true);
			endPane.setManaged(true);
			resize();
		});
	}

	public void displayGame() {
		//Display game board
		Platform.runLater(() -> {
			resultPane.setVisible(false);
			resultPane.setManaged(false);
			sessionPane.setVisible(false);
			sessionPane.setManaged(false);
			endPane.setVisible(false);
			endPane.setManaged(false);
			gamePane.setVisible(true);
			gamePane.setManaged(true);
			resize();
		});
	}

	public void displayResult(String scores, String validWords){
		//Display result screen with players' scores
		Platform.runLater(() -> {
			resultPane.setVisible(true);
			resultPane.setManaged(true);
			sessionPane.setVisible(false);
			sessionPane.setManaged(false);
			endPane.setVisible(false);
			endPane.setManaged(false);
			gamePane.setVisible(false);
			gamePane.setManaged(false);
			resize();

			//Display valided words
			String[] vWords = validWords.split("\\*");
			String words = "";
			for (String string : vWords) {
				words+=string+",";
			}
			foundWordsfinal.setText(words);



			//Round number display
			Label lbl = new Label("Round "+currRound++);lbl.setId("subtitle");fxscores.getChildren().add(lbl);

			//Update score des joueurs
			String[] infos = scores.split("\\*");
			//Cela eût été plus facile avec une hashmap
			for(int i=0;i<infos.length-1;i=i+2) {
				//Update
				for (User us : users) {
					if(us.getName().toUpperCase().equals(infos[i].toUpperCase())) {
						us.setScore(us.getScore()+Integer.parseInt(infos[i+1]));
					}
				}

				//Looking for personal score
				if(infos[i].equals(usernameLabel.getText())) {
					Label sc = new Label("Your score  : " +infos[i+1]);
					sc.setId("data");
					fxscores.getChildren().add(sc);
				}
			}

			//Display aother players' score
			for(int i=0;i<infos.length-1;i=i+2) {
				if(infos[i].equals(usernameLabel.getText())) {

				}
				else {
					Label pl = new Label(infos[i] +" : " +infos[i+1]);
					pl.setId("data");
					fxscores.getChildren().add(pl);
				}
			}

			//Update userlist visual
			ObservableList<User> userz = FXCollections.observableList(users);
			userList.setItems(userz);
			userList.setCellFactory(new CellRenderer());

		});


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

	public void resize() {
//		MainLauncher.getPrimaryStage().setWidth(MainLauncher.getPrimaryStage().getMaxWidth());
	//	MainLauncher.getPrimaryStage().setHeight(MainLauncher.getPrimaryStage().getMaxHeight());
	//	MainLauncher.getPrimaryStage().setMaximized(true);
		MainLauncher.getPrimaryStage().setHeight(700);
		MainLauncher.getPrimaryStage().setWidth(1200);
	}

	public void resetScore() {
		for (User us : users) {
			us.setScore(0);
		}
	}

	public void setCurrentWord(String string) {
		Platform.runLater(()-> currentWord.setText(string));;
	}

	//	private void bindToTime(Object o) {
	//		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0),
	//				event -> ((Labeled) o).setText(LocalTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME))),
	//				new KeyFrame(Duration.seconds(1)));
	//
	//		timeline.setCycleCount(Animation.INDEFINITE);
	//		timeline.play();
	//	}

}