package skyjo.server.secondaryServers;

import skyjo.exception.DBBusinessException;
import skyjo.exception.DBException;
import skyjo.playerMsg.*;
import skyjo.skyjoPlayer.*;
import skyjo.serverMsg.*;
import skyjo.model.*;
import skyjo.otherMsg.OtherMsg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * The type Player thread.
 */
public class PlayerThread extends Thread {

    private static SrvMsgType NO_OBJECT_TO_SEND;
    private User user;
    private final SecondaryServer srv;
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final List<ServerMsg> msgList;
    private boolean running;
    private boolean playing;

    /**
     * Constructor for a client thread
     *
     * @param srv the game server that create the thread
     * @param clientSocket the client socket
     */
    public PlayerThread(SecondaryServer srv, Socket clientSocket) {
        this.user = null;
        this.srv = srv;
        this.clientSocket = clientSocket;
        msgList = new ArrayList<>();
        this.playing = false;
        fillMsgList();
    }

    /**
     * initialize input and output stream for communication
     */
    private void establishedConnexion() {
        try {
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * begin communication with the client
     */
    private void startCommunication() throws DBBusinessException, DBException {
        try {
            MsgType temp;
            do {
                System.out.println("Listening input stream...");
                var usrMsg = (UserMsg) inputStream.readObject();
                temp = (MsgType) usrMsg.getType();
                
                switch ((MsgType) usrMsg.getType()) {
                    case CLICK_START -> {
                        this.srv.createGame();
                        
                    }

                    case CLICK_DISCARD -> {
                        this.srv.applyDiscardAction((Card) usrMsg.getContent());
                        updateSwitchSend();
                    }
                    case CLICK_PITCHED -> {
                        this.srv.applyPitchedAction(usrMsg);
                        updateSwitchSend();
                    }
                    case ASK_CARD -> {
                        var card = this.srv.makeHit();
                        sendToClient(card);
                    }
                    case UPDATE_MAIL ->
                        this.srv.createUser((String) usrMsg.getContent());
                    case ASK_PLAYER_INFO -> {

                        var response = this.srv.playerAskFor((String) usrMsg.getContent());
                        if (response != null) {
                            sendToClient(response);
                        }

                    }
                }
                this.srv.showStatus(this, temp);
            } while (running);

        } catch (IOException | ClassNotFoundException e) {
            if (e instanceof IOException) {
                this.srv.handleUserDisconnected(this);
            }
        }
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Is playing boolean.
     *
     * @return the boolean
     */
    public boolean isPlaying() {
        return playing;
    }

    /**
     * Sets playing.
     *
     * @param playing the playing
     */
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    /**
     * return true if no user Object is created
     *
     * @return true if user null
     */
    public boolean userIsNUll() {
        return user == null;
    }

    /**
     * update the game score switch the players if the game is not over and if
     * over notify and stop all session
     */
    private void updateSwitchSend() throws DBBusinessException, DBException {
        this.srv.updateScore();
        if (!this.srv.gameIsOver()) {
            this.srv.switchPlayers();
            this.srv.notifyEveryBodyGameUpdate();
        } else {
            this.srv.notifyEveryBodyGameUpdate();
            this.srv.closedSession();
        }
    }

    /**
     * getter for user ID
     *
     * @return id id
     */
    public int get_id() {
        return user.getID();
    }

    /**
     * Get player server id int.
     *
     * @return the int
     */
    public int getPlayerServerId() {
        return user.getServerId();
    }

    /**
     * getter user mail
     *
     * @return the player mail
     */
    public String getMail() {
        return user.getMail();
    }

    /**
     * function that send a message to a client using the protocol implemented
     * for it
     *
     * @param arg the message that need to be sent ( it may be null if nothing
     * need to be send)
     */
    public void sendToClient(Object arg) {
        try {
            var correctSrvMsg = arg == null ? getCorrectMsg(NO_OBJECT_TO_SEND)
                    : getCorrectMsg(this.srv.getGame().getType());
            if (arg == null) {
                switch (NO_OBJECT_TO_SEND) {
                    case USER_CREATED -> {
                        List<Integer> ans
                                = this.srv.getUserStats(this.getPlayerServerId());
                        System.out.println(ans.isEmpty());
                        arg = new OtherMsg(user, ans);
                        //arg = user;
                    }
                }
            }
            correctSrvMsg.setMsg(arg);
            outputStream.reset();
            outputStream.writeObject(correctSrvMsg);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    /**
     * Override run for thread
     */
    @Override
    public void run() {
        running = true;
        establishedConnexion();
        try {
            startCommunication();
        } catch (DBBusinessException | DBException ex) {
            Logger.getLogger(PlayerThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Function that close socket and the stream of the client
     *
     * @throws IOException exception Thrown by the function
     */
    public void closeConnexion() throws IOException {
        running = false;
        try {
            if (clientSocket != null) {
                clientSocket.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }
        } finally {
            outputStream = null;
            inputStream = null;
            clientSocket = null;

        }

    }

    /**
     * setter for user attribute
     *
     * @param user new user object that need to be update
     */
    public void setUser(User user) {
        this.user = Objects.requireNonNull(user, "User must not be null");
    }

    /**
     * Determined the adequate msg that need to be sent to client using the
     * server type value given as argument cause each msg protocol has a type
     * which is associated to a value with that value we can determine the type
     * of msg that need to be sent
     *
     * @param serverTypeValue value of the SrvType
     * @return a serverMsg that will be sent to the client
     */
    private ServerMsg getCorrectMsg(SrvMsgType type) {

        var serverMsg = msgList.stream().filter(x -> x.getType().
                equals(type)).collect(Collectors.toList());
        return serverMsg.get(0);
    }

    /**
     * Set the value of the NO_OBJECT_TO_SEND variable it's mainly used to
     * specify when their is no object to send to the client
     *
     * @param value the value given by the server
     */
    public void setNoObjectToSend(SrvMsgType value) {
        NO_OBJECT_TO_SEND = value;
    }

    /**
     * Create all the server Message and put it inside a list it helps us not
     * instantiate a new server Msg at any time we want to send a msg
     */
    private void fillMsgList() {
        Arrays.stream(SrvMsgType.values()).
                forEach(x -> msgList.add(new ServerMsg(x)));
    }
}
