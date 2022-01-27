package skyjo.dbspecification;

/**
 *
 * @author mad8
 */
public class UserSpecification {
    private int id;
    private String mail;
    /**
     *
     * @param mail

     */
    public UserSpecification(String mail) {
        this.id = 0;
        this.mail = mail;
    }

    /**
     *
     * @param id
     */
    public UserSpecification(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getMail() {
        return mail;
    }

    /**
     *
     * @param mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }
}


