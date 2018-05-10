package main.client.chatwindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.util.Duration;
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
	private static OutputStream os;
	static Writer out;
	BufferedReader in;
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
			System.out.println("Establishing connection");
			socket = new Socket(hostname, port);
			LoginController.getInstance().showScene();
			os = socket.getOutputStream();
			//oos = new ObjectOutputStream(os);
			is = socket.getInputStream();
			//input = new ObjectInputStream(is);
			in = new BufferedReader(new InputStreamReader(is));
			out = new BufferedWriter(new OutputStreamWriter(os));
			System.out.println("Connected");
			logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
			try {
				connect();           
				logger.info("Sockets in and out ready!");
				while (socket.isConnected()) {
					
					MessageType msgtype;
					Message message = null;
					String transmission = null;
					try {
						//message = (Message) input.readObject();
						transmission = in.readLine();
						Thread.sleep(250);
						
					}
					catch(EOFException e) {

					}

					if (transmission != null) {
						String[] infos;
						System.out.println("server :: "+transmission);
						String[] parts = transmission.split("/");
//						for (String string : parts) {
//							System.out.println("part:  "+string);
//						}
						switch(parts[0]){
						
						case "BIENVENUE":
							infos = parts[2].split("\\*");
							int nbTours =  Integer.parseInt(infos[0]);
							int nbUser = (infos.length/2);
							String[] users = new String[nbUser];
							int[] scores = new int[nbUser];
							int cpt = 0;
							
							for(int i=1; i < infos.length; i = i+2) {
								users[cpt]=infos[i];
								scores[cpt]=Integer.parseInt(infos[i+1]);
								cpt++;
							}
							
							controller.setMatrix(parts[1]);
							controller.setUserListRaw(users,scores);
							//TODO Scores
							msgtype = MessageType.CONNECTED;
							
							break;
							
						case "CONNECTE":
							//Pour dire bonjour automatiquement
							//controller.addUserChat("Bonjour !", parts[1]);
							controller.addUserToList(parts[1]);
							controller.newUserEntryNotification(parts[1]);
							msgtype = MessageType.USER;
							break;
							
						case "DECONNEXION":
							controller.removeUserFromList(parts[1]);
							controller.userDisconnectedNotification(parts[1]);
							msgtype = MessageType.DISCONNECTED;
							//Action retrait joueur du chat
							break;
							
						case "SESSION":
							//debut session
							controller.displayMatrix(true);
							break;
							
						case "VAINQUEUR":
							//rception scores fin
							break;
							
						case "TOUR":
							//recepetion nouveau tirage
							controller.setMatrix(parts[1]);
							break;
							
						case "MVALIDE":
							//mot proposé validé
							break;
							
						case "MINVALIDE":
							//mot proposé invalidé
							break;
							
						case "RFIN":
							//Fin du tour
							controller.displayMatrix(false);
							break;
							
						case "BILANMOTS":
							//Résultats mots proposés
							controller.displayResult(parts[1]);
							break;
							
						case "RECEPTION":
							//Reception message
							controller.addUserMessage(parts[2],parts[1]);
							break;
							
						case "PRECEPTION":
							//Reception message privé
							break;
						
						case "STATUS":
							//Notification de changement de status
							controller.updateUserStatus(parts[1],parts[2]);
							break;
						
						default:
							break;


						}

						//						logger.debug("Message received:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
						//						switch (message.getType()) {
						//						case USER:
						//							controller.addToChat(message);
						//							break;
						//						case VOICE:
						//							controller.addToChat(message);
						//							break;
						//						case NOTIFICATION:
						//							controller.newUserNotification(message);
						//							break;
						//						case SERVER:
						//							controller.addAsServer(message);
						//							break;
						//						case CONNECTED:
						//							System.out.println("Connected");
						//							controller.setUserList(message);
						//							break;
						//						case DISCONNECTED:
						//							controller.setUserList(message);
						//							break;
						//						case STATUS:
						//							controller.setUserList(message);
						//							break;
						//						case MATRIX:
						//							controller.setMatrix(message.getMsg());
						//						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				showErrorDialog("Connection Error", "Your client has been disconnected !");
				controller.logoutScene();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		} catch (IOException e) {
			LoginController.getInstance().showErrorDialog("Could not connect to server","Please check for firewall issues and check if the server is running.");
			logger.error("Could not Connect");
		}


	}


	public static void sendRaw(String msg) throws IOException {
		System.out.println(msg);
		out.append(msg);
		//out.append("\n");
		out.flush();
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
		//	sendRaw("ENVOI/" + createMessage.getName()+"/"+ createMessage.getMsg()+"\n");
		//		oos.writeObject(createMessage);
		//		oos.flush();
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
		//		sendRaw("ENVOIV/" + createMessage.getName()+"/"+ "voiceMessages");
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
		sendRaw("STATUS/"+username+"/"+status+"\n");
	}

	/* This method is used to send a connecting message */
	public static void connect() throws IOException {
		//		Message createMessage = new Message();
		//		createMessage.setName(username);
		//		createMessage.setType(MessageType.CONNECTED);
		//		createMessage.setMsg(HASCONNECTED);
		//		createMessage.setPicture(picture);
		//		oos.writeObject(createMessage);
		sendRaw("CONNEXION/"+username+"/\n");
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
