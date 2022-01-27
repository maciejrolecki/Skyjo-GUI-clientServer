package skyjo.view.viewJfx.fxmlControllers;

import skyjo.client.ClientConnexion;
import skyjo.skyjoPlayer.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserInfo implements Initializable {

    @FXML
    private Label userMail;
    @FXML
    private Label id;
    @FXML
    private Label type;
    @FXML
    private Label lowestScore;
    @FXML
    private Label gamesPlayed;
    @FXML
    private Label winningGames;
    private final User user;
    private final List<Integer> info;

    /**
     * Default constructor 
     * @param user the current user
     * @param info
     */
    public UserInfo(ClientConnexion user,List<Integer> info) {
        this.user = user.getUser();
        this.info = info;
    }

    /**
     * Override function from Initializable
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userMail.setText(user.getMail());
        id.setText(String.valueOf(user.getID()));
        type.setText(String.valueOf(user.getType()));
        lowestScore.setText(String.valueOf(info.get(2)));
        gamesPlayed.setText(String.valueOf(info.get(1)));
        winningGames.setText(String.valueOf(info.get(0)));

    }

}
