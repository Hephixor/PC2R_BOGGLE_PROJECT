package main.client.util;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import main.exceptions.EmptyWordException;
import main.exceptions.FullWordException;

public class Word {
	private ArrayList<Character> letters;
	private ArrayList<String> trail; 

	public Word() {
		this.letters = new ArrayList<Character>();
		this.trail = new ArrayList<String>();
	}

	public void addLetter(char letter, String pos) {
		try {
			if(letters.size()==16) {
				throw new FullWordException("Error word is the maximum length");
			}
			else {
			letters.add(letter);
			trail.add(pos);
			}
		} catch (FullWordException e) {
			showErrorDialog("Invalid Move !", "Your word has reached the maximul length.");
		}
	}
	
	public void removeLetter() {
		try {
			if(letters.size()==0) {
				throw new EmptyWordException("Error word is empty");
			}
			else {
			letters.remove(letters.size()-1);
			trail.remove(trail.size()-1);
			}
		} catch (EmptyWordException e) {
			showErrorDialog("Invalid Move !", "Your word is empty");
		}
	}
	
	public void resetBoard() {
		letters.removeAll(getLetters());
		trail.removeAll(getTrail());
	}
	
	public ArrayList<Character> getLetters(){
		return letters;
	}
	
	public ArrayList<String> getTrail(){
		return trail;
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
