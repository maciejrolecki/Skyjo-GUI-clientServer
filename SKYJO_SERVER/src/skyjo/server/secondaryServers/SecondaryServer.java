package skyjo.server.secondaryServers;

import skyjo.playerMsg.*;
import skyjo.skyjoPlayer.*;
import skyjo.model.*;
import skyjo.otherMsg.OtherMsg;
import skyjo.server.motherServer.PrimaryServer;
import skyjo.serverMsg.SrvMsgType;
import skyjo.business.AdminModel;
import skyjo.exception.DBBusinessException;
import skyjo.exception.DBException;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecondaryServer {

    private static final int MAX_PLAYERS = 8;
    private static final int MIN_PLAYERS = 2;
    private PlayerThread firstPlayerConnected;
    private PlayerThread sndPlayerConnected;
    private Facade game;
    private final int srvID;
    private boolean full;
    private final GameMembers members;
    private final List<PlayerThread> playerThreads;
    private int memberID;
    private boolean gameStarted;
    private final PrimaryServer server;
    private boolean notify;
    private final List<Player> tempThread;
    private final AdminModel dbAccess;

    /**
     * @param srvID the id given by the main Server to identify him
     * @param server
     * @brief Construct the skyjo second server is the middle server that handle
     * a game with all his players
     */
    public SecondaryServer(int srvID, PrimaryServer server) {
        this.srvID = srvID;
        this.full = false;
        this.members = new GameMembers();
        this.playerThreads = new ArrayList<>();
        tempThread = new ArrayList<>();
        gameStarted = false;
        this.server = server;
        this.notify = false;
        dbAccess = new AdminModel();
    }

    /**
     * card object hit by the game
     *
     * @return Card Object
     */
    public Card makeHit() {
        return this.game.hit();
    }

    /**
     * getter of full Boolean to know if the server is can still receive client
     * or not
     *
     * @return Boolean true if the server can't receive and false if he can
     */
    public boolean isFull() {
        return full;
    }

    /**
     * Create a new user and add it to the members of the game
     *
     * @param name the name of the client connected
     * @param type userType if it's the first client connected the type will be
     * admin and if not the type will be other
     */
    private void addUser(String userMail, UserType type) throws DBBusinessException {
        memberID++;
        int servid = dbAccess.getUserId(userMail);
        members.addUser(new User(userMail, memberID, type, servid));
    }

    /**
     * the method to call when a new client is connected and need to join a game
     * session
     *
     * @param clientSocket the socket of the client connected
     */
    public void receivedData(Socket clientSocket) {
        this.playerThreads.add(new PlayerThread(this, clientSocket));
        this.playerThreads.stream().
                filter(x -> x.getState() == Thread.State.NEW).
                forEach(Thread::start);

    }

    /**
     * return true if theirs no user in the member object and false if not
     *
     * @return Boolean true/false
     */
    private boolean noMembers() {
        return members.noUsers();
    }

    /**
     * getter for the game
     *
     * @return Model ( game )
     */
    public Facade getGame() {
        return game;
    }

    /**
     * update if the game is over ( score and winner)
     *
     * @throws DBBusinessException
     * @throws DBException
     */
    private void gameIsOverDB() throws DBBusinessException, DBException {
        List<Integer> usersId = new ArrayList();
        List<Integer> usersscore = new ArrayList();
        this.members.getUsers().forEach(user -> {
            usersId.add(user.getServerId());
            usersscore.add(this.game.getPlayerWithMail(members.findAUser(user.getID()).getMail()).getScore());
        });
        int winnerId = this.game.getWinner().getPlayerServerId();
        dbAccess.updateUserScores(usersId, usersscore);
        dbAccess.addWin(winnerId);
        dbAccess.addDataFromGame(usersId, usersscore, this.srvID);

    }

    /**
     * return true if game is over or not
     *
     * @return Boolean true/false
     * @throws skyjo.exception.DBBusinessException
     * @throws skyjo.exception.DBException
     */
    public boolean gameIsOver() throws DBBusinessException, DBException {
        boolean ret = this.game.isOver();
        if (ret) {
           // gameIsOverDB();
        }
        return ret;
    }

    /**
     * return the state of the gameStarted attribute
     *
     * @return Boolean true/false
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * method use to create user and send msg to the client when it's created
     * for them to know their status
     *
     * @param userMail
     * @throws skyjo.exception.DBBusinessException
     */
    public void createUser(String userMail) throws DBBusinessException {
        if (!userMail.isEmpty()) {
            if (noMembers()) {
                firstPlayerConnected = getCurrentPlayerThread();
                addUser(userMail, UserType.ADMIN);
                firstPlayerConnected.setUser(members.findAUser(1));
                firstPlayerConnected.setNoObjectToSend(SrvMsgType.USER_CREATED);
                firstPlayerConnected.sendToClient(null);
            } else {
                if (playerThreads.size() == MAX_PLAYERS) {
                    full = true;
                    checkGameStarted(userMail);
                } else {
                    if (isNotValid(userMail)) {
                        var playerThread = getCurrentPlayerThread();
                        playerThread.setNoObjectToSend(SrvMsgType.INVALID_MAIL);
                        playerThread.sendToClient(null);
                    } else {
                        if (twoClientOnly()) {
                            notifyAllPlayerOnlineStatus();
                            sendMsgToFirstAndSndPlayerConnected(userMail);
                        } else {
                            checkGameStarted(userMail);
                        }
                    }
                }

            }
        } else {
            var playerThread = getCurrentPlayerThread();
            playerThread.setNoObjectToSend(SrvMsgType.INVALID_MAIL);
            playerThread.sendToClient(null);

        }
    }

    /**
     * check if the game as started or not
     *
     * @param userMail
     * @throws DBBusinessException
     */
    private void checkGameStarted(String userMail) throws DBBusinessException {
        if (gameStarted) {
            addNewUser(userMail);
        } else {
            if (notify) {
                addUserWithOutGameCreated(userMail);
            } else {
                PlayerThread admin = getAdmin();
                if (admin != null) {
                    notify = true;
                    admin.setNoObjectToSend(SrvMsgType.ALL_PLAYER_ONLINE);
                    admin.sendToClient(null);
                    sendMsgToFirstAndSndPlayerConnected(userMail);
                }
            }
        }
    }

    /**
     * boolean check if they are 2 player
     *
     * @return
     */
    private boolean twoClientOnly() {
        return playerThreads.size() == MIN_PLAYERS;
    }

    /**
     * Send a message to the Admin user to notify him when a new player is
     * online
     */
    private void notifyAllPlayerOnlineStatus() {
        if (firstPlayerConnected != null) {
            notify = true;
            firstPlayerConnected.setNoObjectToSend(SrvMsgType.ALL_PLAYER_ONLINE);
            firstPlayerConnected.sendToClient(null);
        }
    }

    /**
     * add an user
     *
     * @param userMail
     * @throws DBBusinessException
     */
    private void addUserWithOutGameCreated(String userMail) throws DBBusinessException {
        addUser(userMail, UserType.OTHER);
        var currentThread = getCurrentPlayerThread();
        currentThread.setUser(members.findAUser(memberID));
        tempThread.add(new Player(
                new PlayerDetails(
                        currentThread.get_id(),
                        currentThread.getPlayerServerId(),
                        currentThread.getMail())));
        currentThread.setNoObjectToSend(SrvMsgType.USER_CREATED);
        currentThread.sendToClient(null);

    }

    /**
     * get the admin thread
     *
     * @return admin Player Thread
     */
    private PlayerThread getAdmin() {
        return playerThreads.stream().
                filter(x -> {
                    return x.getUser() != null
                            && x.getUser().getType() == UserType.ADMIN;
                }).
                toList().get(0);

    }

    /**
     * Send a user created type message to the user that is newly created and
     * send to the admin user that the user is ready and can start the game
     *
     * @param name name given by the new player connected
     */
    private void sendMsgToFirstAndSndPlayerConnected(String userMail) throws DBBusinessException {
        addUser(userMail, UserType.OTHER);
        sndPlayerConnected = getCurrentPlayerThread();
        sndPlayerConnected.setUser(members.findAUser(memberID));
        sndPlayerConnected.setNoObjectToSend(SrvMsgType.USER_CREATED);
        
        sndPlayerConnected.sendToClient(null);
        firstPlayerConnected.setNoObjectToSend(SrvMsgType.USER_READY);
        firstPlayerConnected.sendToClient(null);

    }

    /**
     * return the playerThread that is running to apply some modification
     *
     * @return PlayerThread Object
     */
    private PlayerThread getCurrentPlayerThread() {
        return playerThreads.stream().filter(x -> x.getId()
                == Thread.currentThread().
                        getId()).toList().get(0);
    }

    /**
     * return true if the name given is already in the members
     *
     * @param name of the player newly connected
     * @return true if match false if not
     */
    private boolean isNotValid(String name) {
        return members.getUsers().stream().
                anyMatch(x -> x.getMail().contains(name));

    }

    /**
     * Create a new object of game and send to all players online
     */
    public void createGame() {
        this.game = new Game(new PlayerDetails(firstPlayerConnected.get_id(),
                firstPlayerConnected.getPlayerServerId(),
                firstPlayerConnected.getMail()),
                new PlayerDetails(sndPlayerConnected.get_id(),
                        sndPlayerConnected.getPlayerServerId(),
                        sndPlayerConnected.getMail()));
        if (tempThread.isEmpty()) {
            this.game.revealCard();
        } else {
            this.game.addNewPlayers(tempThread);
            this.game.updatePlayers();
        }

        gameStarted = true;
        playerThreads.forEach(player -> {
            player.setPlaying(true);
            player.sendToClient(this.game);
        });
    }

    /**
     * Apply the discard action according to the card received
     *
     * @param card send by the player
     */
    public synchronized void applyDiscardAction(Card card) {
        try {
            this.game.setCardCurrentPlayer(card);
            this.game.updateGame(PlayerChoice.DISCARD);
        } catch (IllegalStateException e) {
            System.out.println("Error");
        }
    }

    /**
     * update the score of the game
     */
    public synchronized void updateScore() {
        this.game.updateScore();
    }

    /**
     * switch the players
     */
    public synchronized void switchPlayers() {
        this.game.switchPlayer();
    }

    /**
     * send the game to all the players for further update
     */
    public void notifyEveryBodyGameUpdate() {
        this.playerThreads.
                forEach(playerThread -> playerThread.sendToClient(this.game));
    }

    /**
     * return a player information ask by a player
     *
     * @param mail
     * @return Player
     */
    public synchronized Player playerAskFor(String mail) {
        if (mail != null) {
            return this.game.getPlayerWithMail(mail);

        }
        return null;

    }

    /**
     * apply the pitched action
     *
     * @param msg the message received by the player
     */
    public synchronized void applyPitchedAction(UserMsg msg) {
        var cardReceived = (Card) msg.getContent();
        var pickOption = msg.getPlayerPickOption();
        try {
            PlayerChoice choice = null;
            switch (pickOption) {
                case 1 ->
                    choice = PlayerChoice.PICK_KEEP;
                case -1 ->
                    choice = PlayerChoice.PICK_DROP;
            }
            game.setCardCurrentPlayer(cardReceived);
            game.updateGame(choice);
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    /**
     * Show information about thread and message received
     *
     * @param thread the thread running
     * @param type the Message type send by the player
     */
    public void showStatus(PlayerThread thread, MsgType type) {
        System.out.println("-----------SHOW GAME MANAGER STATE---------------");
        System.out.println("ID gestionnaire du jeu en cours: " + srvID);
        System.out.println("Jeu en cours ?: " + gameStarted);
        System.out.println("Nombre de joueur: " + playerThreads.size());
        System.out.println("Type de message:" + type);
        System.out.println("Nom du thread courant : " + thread.getName());
        if (thread.getUser() != null) {
            System.out.println("Nom du joueur :" + thread.getMail());
            System.out.println("ID joueur : " + thread.get_id());
            System.out.println("ID joueur sur db : " + thread.getUser().getServerId());
        }
        System.out.println("-------------------------------------------------------\n");

    }

    /**
     * This function handle the way a client is disconnected
     *
     * @param thread the current thread that stop
     * @throws skyjo.exception.DBBusinessException
     * @throws skyjo.exception.DBException
     */
    public void handleUserDisconnected(PlayerThread thread) throws DBBusinessException, DBException {
        if (!thread.userIsNUll()) {
            System.out.println("User Disconnected | Name : " + thread.
                    getMail() + "| ID: " + thread.get_id() + " |");
            members.removeUser(thread.get_id());
            if (game != null) {
                if (!gameIsOver()) {
                    if (game.getCurrentPlayer().getPlayerID() == thread.get_id()) {
                        game.switchPlayer();
                    }
                    this.game.removePlayer(thread.get_id());
                    playerThreads.stream().
                            filter(x -> x.getId() != thread.getId()).
                            forEach(x -> x.sendToClient(this.game));
                }
            }
            try {
                thread.closeConnexion();
                this.playerThreads.remove(thread);
            } catch (IOException ex) {
                Logger.getLogger(SecondaryServer.class.
                        getName()).log(Level.SEVERE, null, ex);
            }
            if (this.playerThreads.isEmpty()) {
                this.server.stopMySession(this);
            }

        }
    }
        /**
         * This function close all the connection of all the thread connected
         * and ask the mother server to stop
         */
    public void closedSession() {
        playerThreads.forEach((PlayerThread x) -> {
            try {
                x.closeConnexion();
            } catch (IOException ex) {
                Logger.getLogger(SecondaryServer.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
        this.server.stopMySession(this);
    }

    /**
     * add a new user to the game
     *
     * @param name user name
     */
    private void addNewUser(String name) throws DBBusinessException {
        synchronized (this) {
            addUser(name, UserType.OTHER);
            var user = members.findAUser(memberID);
            this.game.
                    addNewPlayer(new Player(new PlayerDetails(user.
                            getID(), user.getServerId(), user.getMail())));
            var playerThread = getCurrentPlayerThread();
            playerThread.setUser(user);
            playerThreads.stream().
                    filter(x -> {
                        return x.getUser() != null && x.get_id()
                                != playerThread.get_id() && x.isPlaying();
                    }).
                    forEach(x -> x.sendToClient(this.game));
            playerThread.sendToClient(new OtherMsg(user, this.game));

        }
    }

    /**
     * getter of the stat of the user
     *
     * @param idUser
     * @return
     */
    public List<Integer> getUserStats(int idUser) {
        try {
            return dbAccess.getUserStats(idUser);
        } catch (DBBusinessException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
