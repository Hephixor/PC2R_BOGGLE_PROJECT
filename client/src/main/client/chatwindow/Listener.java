package main.client.chatwindow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import main.client.login.LoginController;
import main.messages.Status;

public class Listener implements Runnable{

	private Socket socket;
	public String hostname;
	public int port;
	public static String username;
	public ChatController controller;
	private InputStream is;
	private static OutputStream os;
	static Writer out;
	BufferedReader in;
	int nbTours;
	static Logger logger = LoggerFactory.getLogger(Listener.class);

	public Listener(String hostname, int port, String username, String picture, ChatController controller) {
		this.hostname = hostname;
		this.port = port;
		Listener.username = username;
		this.controller = controller;
	}

	public void run() {
		try {
			System.out.print("Establishing connection... ");
			socket = new Socket(hostname, port);
			LoginController.getInstance().showScene();
			os = socket.getOutputStream();
			//oos = new ObjectOutputStream(os);
			is = socket.getInputStream();
			//input = new ObjectInputStream(is);
			in = new BufferedReader(new InputStreamReader(is));
			out = new BufferedWriter(new OutputStreamWriter(os));
			System.out.println("connected !");
			logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
			try {
				connect();           
				logger.info("Sockets in and out ready!");
				while (socket.isConnected()) {
					controller.resize();
					String transmission = null;
					try {
						transmission = in.readLine();
					}
					catch(EOFException e) {

					}

					if (transmission != null) {
						String[] infos;
						logger.info("server :: "+transmission);
						String[] parts = transmission.split("/");
						
						switch(parts[0]){

						case "BIENVENUE":
							infos = parts[2].split("\\*");
							nbTours =  Integer.parseInt(infos[0]);
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
							controller.displayGame();
							
							break;

						case "CONNECTE":
							//Pour dire bonjour automatiquement
							//controller.addUserChat("Bonjour !", parts[1]);
							controller.addAsServer(parts[1] + " connected");
							controller.addUserToList(parts[1]);
							controller.newUserEntryNotification(parts[1]);
							break;

						case "DECONNEXION":
							controller.removeUserFromList(parts[1]);
							controller.userDisconnectedNotification(parts[1]);
							//Action retrait joueur
							break;

						case "SESSION":
							//debut session
							controller.displaySession();
							controller.resetScore();
							break;

						case "VAINQUEUR":
							//reception scores fin
							controller.displayEnd(parts[1]);
							break;

						case "TOUR":
							//reception nouveau tirage
							controller.setMatrix(parts[1]);
							controller.displayMatrix(true);
							controller.resetWords();
							controller.displayGame();
							break;

						case "MVALIDE":
							//mot proposé validé
							controller.addValidWord(parts[1]);
							break;

						case "MINVALIDE":
							//mot proposé invalidé
							controller.addInvalidWord(parts[1]);
							break;

						case "RFIN":
							//Fin du tour
							controller.displayMatrix(false);
							controller.resetBoardAction();
							break;

						case "BILANMOTS":
							//Résultats mots proposés
							controller.displayResult(parts[2],parts[1]);
							break;

						case "RECEPTION":
							//Reception message
							if(!(parts[2].toUpperCase().equals(username.toUpperCase()))) {
								controller.addUserMessage(parts[1],parts[2]);
							}
							break;

						case "PRECEPTION":
							//Reception message privé
							controller.addPrivateMessage(parts[1],username,parts[2]);
							break;

						case "STATUS":
							//Notification de changement de status
							controller.updateUserStatus(parts[1],parts[2]);
							break;

						default:
							logger.error("Unrecognized server command :: "+transmission);
							break;
						}
					}
				}
			} catch (IOException e) {
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
		logger.info("client :: " + msg);
		out.append(msg);
		//out.append("\n");
		out.flush();
	}

	public static void sendVoiceMessage(byte[] audio) throws IOException {
		sendRaw("ENVOIV/" + username+"/"+ audio+"\n");
	}

	public static void sendStatusUpdate(Status status) throws IOException {
		sendRaw("STATUS/"+username+"/"+status+"\n");
	}

	public static void connect() throws IOException {
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
