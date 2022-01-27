package skyjo.playerMsg;

import java.io.Serializable;

/**
 * The UserType represents the type of a message send between a player and the
 * server.
 */
public enum MsgType implements Serializable {
    /**
     * the type of message that is send when the admin of the game click on
     * start game button in the view
     */
    CLICK_START,
    /**
     * when the player choose to make a discard action.
     */
    CLICK_DISCARD,
    /**
     * when the player decided to make a pitched action.
     */
    CLICK_PITCHED,
    /**
     * When the client send his mail to the server to create a user
     */
    UPDATE_MAIL,
    /**
     * ask a card to the server
     */
    ASK_CARD,
    /**
     * ask a information about a player
     */
    ASK_PLAYER_INFO;

}
