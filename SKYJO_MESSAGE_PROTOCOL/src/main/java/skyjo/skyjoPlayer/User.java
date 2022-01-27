package skyjo.skyjoPlayer;

import java.io.Serializable;
/**
 * a user online and connected
 * @author 
 */
public class User implements Serializable {

    public static final int DEFAULT_ID = 0;
    private final String mail;
    private int ID;
    private int serverId; //ID of the user on the DB

    private UserType type;

    /**
     * Constructor of the user having his mail and ID
     * @param mail of the player 
     * @param ID of the player
     */
    public User(String mail, int ID) {
        this.mail = mail;
        this.ID = ID;
        type = UserType.GUEST;
    }

    /**
     * Overload constructor
     * @param mail of the player
     * @param ID of the player
     * @param type ADMIN or OTHER or GUEST
     */
    public User(String mail, int ID, UserType type, int servid) {
        this.mail = mail;
        this.ID = ID;
        this.type = type;
        this.serverId = servid;
    }

    /**
     * Overload constructor using the mail only
     * @param mail of the player 
     */
    public User(String mail) {
        this.mail = mail;
        this.ID = DEFAULT_ID;
        this.type = UserType.GUEST;
    }

    /**
     * setter of ID
     * @param ID of the player
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * setter of the userType
     * @param type userType
     */
    public void setType(UserType type) {
        this.type = type;
    }

    public void setServerId(int serverID) {
        this.serverId = serverID;
    }
    
    /**
     * getter of the player mail
     * @return mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * getter of the id 
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * getter of the user Type
     * @return userType
     */
    public UserType getType() {
        return type;
    }
    
    public int getServerId() {
        return serverId;
    }
}
