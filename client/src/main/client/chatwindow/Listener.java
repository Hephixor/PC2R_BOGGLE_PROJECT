package main.client.chatwindow;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import main.client.login.LoginController;
import main.messages.Message;
import main.messages.MessageType;
import main.messages.Status;

public class Listener implements Runnable{

	private static final String HASCONNECTED = "has connected";

	private static String picture;
	private Socket socket;
	public String hostname;
	public int port;
	public static String username;
	public ChatController controller;
	private static ObjectOutputStream oos;
	private InputStream is;
	private ObjectInputStream input;
	private OutputStream outputStream;
	Logger logger = LoggerFactory.getLogger(Listener.class);

	public Listener(String hostname, int port, String username, String picture, ChatController controller) {
		this.hostname = hostname;
		this.port = port;
		Listener.username = username;
		Listener.picture = picture;
		this.controller = controller;
	}

	public void run() {
		try {
			socket = new Socket(hostname, port);
			LoginController.getInstance().showScene();
			outputStream = socket.getOutputStream();
			oos = new ObjectOutputStream(outputStream);
			is = socket.getInputStream();
			input = new ObjectInputStream(is);
			logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
			try {
				connect();           
				logger.info("Sockets in and out ready!");
				while (socket.isConnected()) {
					Message message = null;
					try {
						message = (Message) input.readObject();
					}
					catch(EOFException e) {

					}

					if (message != null) {
						logger.debug("Message received:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
						switch (message.getType()) {
						case USER:
							controller.addToChat(message);
							break;
						case VOICE:
							controller.addToChat(message);
							break;
						case NOTIFICATION:
							controller.newUserNotification(message);
							break;
						case SERVER:
							controller.addAsServer(message);
							break;
						case CONNECTED:
							controller.setUserList(message);
							break;
						case DISCONNECTED:
							controller.setUserList(message);
							break;
						case STATUS:
							controller.setUserList(message);
							break;
						case MATRIX:
							controller.setMatrix(message);
						}
					}
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
				showErrorDialog("Connection Error", "Your client has been disconnected !");
				controller.logoutScene();
			}

		} catch (IOException e) {
			LoginController.getInstance().showErrorDialog("Could not connect to server","Please check for firewall issues and check if the server is running.");
			logger.error("Could not Connect");
		}


	}


	public static void sendRaw(String msg) throws IOException {
		System.out.println(msg);
		//		oos.writeObject(msg);
		//		oos.flush();
		//		oos.writeObject(msg);
		//		oos.flush();
	}
	/* This method is used for sending a normal Message
	 * @param msg - The message which the user generates
	 */
	public static void send(String msg) throws IOException {
		Message createMessage = new Message();
		createMessage.setName(username);
		createMessage.setType(MessageType.USER);
		createMessage.setStatus(Status.AWAY);
		createMessage.setMsg(msg);
		createMessage.setPicture(picture);
		sendRaw("ENVOI/" + createMessage.getName()+"/"+ createMessage.getMsg());
		oos.writeObject(createMessage);
		oos.flush();
	}

	/* This method is used for sending a voice Message
	 * @param msg - The message which the user generates
	 */
	public static void sendVoiceMessage(byte[] audio) throws IOException {
		Message createMessage = new Message();
		createMessage.setName(username);
		createMessage.setType(MessageType.VOICE);
		createMessage.setStatus(Status.AWAY);
		createMessage.setVoiceMsg(audio);
		createMessage.setPicture(picture);
		sendRaw("ENVOIV/" + createMessage.getName()+"/"+ "voiceMessages");
//		oos.writeObject(createMessage);
//		oos.flush();
	}

	/* This method is used for sending a normal Message
	 * @param msg - The message which the user generates
	 */
	public static void sendStatusUpdate(Status status) throws IOException {
		Message createMessage = new Message();
		createMessage.setName(username);
		createMessage.setType(MessageType.STATUS);
		createMessage.setStatus(status);
		createMessage.setPicture(picture);
		oos.writeObject(createMessage);
		oos.flush();
		sendRaw("STATUS/"+username+"/"+status);
	}

	/* This method is used to send a connecting message */
	public static void connect() throws IOException {
		Message createMessage = new Message();
		createMessage.setName(username);
		createMessage.setType(MessageType.CONNECTED);
		createMessage.setMsg(HASCONNECTED);
		createMessage.setPicture(picture);
		oos.writeObject(createMessage);
		sendRaw("CONNEXION/"+username+"/");
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
