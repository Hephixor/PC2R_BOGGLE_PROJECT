package main.client.chatwindow;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    @FXML private Label onlineCountLabel;
    @FXML private ListView userList;
    @FXML private ImageView userImageView;
    @FXML private Button recordBtn;
    @FXML ListView chatPane;
    @FXML ListView statusList;
    @FXML BorderPane borderPane;
    @FXML ComboBox statusComboBox;
    @FXML ImageView microphoneImageView;

    
    File imgMicrophoneActive = new File("/home/skylab/UPMC/M1S2/PC2R/PC2R_BOGGLE_PROJECT/client/src/main/resources/images/microphone-active.png");
    File imgMicrophone = new File("/home/skylab/UPMC/M1S2/PC2R/PC2R_BOGGLE_PROJECT/client/src/main/resources/images/microphone.png");
    
    Image microphoneActiveImage = new Image(imgMicrophoneActive.toURI().toString());
    Image microphoneInactiveImage = new Image(imgMicrophone.toURI().toString());

    private double xOffset;
    private double yOffset;
    Logger logger = LoggerFactory.getLogger(ChatController.class);


    public void sendButtonAction() throws IOException {
        String msg = messageBox.getText();
        if (!messageBox.getText().isEmpty()) {
            Listener.send(msg);
            messageBox.clear();
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
                Image image = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture() + ".png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                BubbledLabel bl6 = new BubbledLabel();
                if (msg.getType() == MessageType.VOICE){
                    ImageView imageview = new ImageView(new Image(getClass().getClassLoader().getResource("images/sound.png").toString()));
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
                    bl6.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/sound.png").toString())));
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
            //Image profileImg = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture().toLowerCase() +".png").toString(),50,50,false,false);
            TrayNotification tray = new TrayNotification();
            tray.setTitle("A new user has joined!");
            tray.setMessage(msg.getName() + " has joined the JavaFX Chatroom!");
            tray.setRectangleFill(Paint.valueOf("#2C3E50"));
            tray.setAnimationType(AnimationType.POPUP);
       //     tray.setImage(profileImg);
            tray.showAndDismiss(Duration.seconds(5));
            try {
                File hitFile = new File("/home/skylab/UPMC/M1S2/PC2R/PC2R_BOGGLE_PROJECT/client/src/main/resources/sounds/notification.wav");
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
//        switch (selectedPicture) {
//            case "Dominic":
//                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/Dominic.png").toString()));
//                break;
//            case "Sarah":
//                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/sarah.png").toString()));
//                break;
//            case "Default":
//                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
//                break;
//        }
    }

    public void logoutScene() {
//        Platform.runLater(() -> {
//        	URL fxmll = null;
//        	try {
//        		fxmll = new File("/home/skylab/UPMC/M1S2/PC2R/PC2R_BOGGLE_PROJECT/src/main/resources/views/LoginView.fxml").toURI().toURL();
//			} catch (MalformedURLException e1) {
//				e1.printStackTrace();
//			}    
//        	
//            FXMLLoader fmxlLoader = new FXMLLoader(fxmll);
//            Parent window = null;
//            
//            try {
//                window = (Pane) fmxlLoader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Stage stage = MainLauncher.getPrimaryStage();
//            Scene scene = new Scene(window);
//            stage.setMaxWidth(350);
//            stage.setMaxHeight(420);
//            stage.setResizable(false);
//            stage.setScene(scene);
//            stage.centerOnScreen();
//        });
    }
}