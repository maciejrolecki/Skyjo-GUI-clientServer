package skyjo.dbdto;

import skyjo.exception.DtoException;
/**
 *
 * @author mad8
 */
public class UserDto extends EntityDto<Integer>{
    private String mail;

    /**
     * constructeur d'un user persistant
     *
     * @param id
     * @param mail
     * @throws skyjo.exception.DtoException
     */
    public UserDto(Integer id, String mail) throws DtoException {
        this(mail);
        this.id = id;
    }

    /**
     * constructeur d'un user non persistant
     *
     * @param mail
     * @throws skyjo.exception.DtoException
     */
    public UserDto(String mail) throws DtoException {
        if ( mail == null) {
            throw new DtoException("les attributs login et  name sont obligatoires");
        }
        this.mail = mail;

    }

    /**
     *
     * @return mail
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

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return "[UserDto] (" + getId() + ") " + getMail();
    }
}
