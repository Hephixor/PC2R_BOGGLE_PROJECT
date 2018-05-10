package main.client.chatwindow;


import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import main.messages.User;

/**
 * A Class for Rendering users images / name on the userlist.
 */
class CellRenderer implements Callback<ListView<User>,ListCell<User>>{
        @Override
    public ListCell<User> call(ListView<User> p) {

        ListCell<User> cell = new ListCell<User>(){

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();

                    Text name = new Text(" " + user.getName());

                    ImageView statusImageView = new ImageView();
                    File stsimg = new File("src/main/resources/images/"+ user.getStatus().toString().toLowerCase() + ".png");
                    Image statusImage = new Image(stsimg.toURI().toString(), 16, 16,true,true);
                    statusImageView.setImage(statusImage);

                    ImageView pictureImageView = new ImageView();
                    File pimg = new File("src/main/resources/images/"+ user.getPicture().toLowerCase() + ".png");
                    
                    Image image = new Image(pimg.toURI().toString(),50,50,true,true);
                    pictureImageView.setImage(image);
                    
                    Label score = new Label();
                    score.setAlignment(Pos.CENTER_RIGHT);
                    score.setText("\t"+String.valueOf(user.getScore()));

                    hBox.getChildren().addAll(statusImageView, pictureImageView, name, score);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    

                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}