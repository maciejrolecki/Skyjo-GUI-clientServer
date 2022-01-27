package skyjo.business;

import skyjo.db.UserDB;
import skyjo.dbdto.UserDto;
import skyjo.exception.DBException;
import skyjo.dbspecification.UserSpecification;
import java.util.Collection;
import java.util.List;
/**
 *
 * @author mad8
 */
public class UserBusinessLogic {
    
    /**
     * Insert an user in the database. Returns the user's id.
     *
     * @param mail user's mail.
     * @return the user's id.
     * @throws SQLException if the query failed.
     */
    static int add(String userMail) throws DBException {
        return UserDB.insertDb(userMail);
    }

    /**
     * Removes the given user.
     *
     * @param user user to delete.
     * @throws SQLException if the query failed.
     */
    static void delete(int id) throws DBException {
        UserDB.deleteDb(id);
    }

    /**
     * Updates the given user.
     *
     * @param user user to update.
     * @throws SQLException if the query failed.
     */
    static void update(UserDto user) throws DBException {
        UserDB.updateDb(user);
    }

    /**
     * Returns the unique user with the given mail.
     *
     * @param id user's mail
     * @return the unique user with the given mail.
     * @throws SQLException if the query failed.
     */
static UserDto findByMail(String mail) throws DBException {
        UserSpecification sel = new UserSpecification(mail);
        Collection<UserDto> col = UserDB.getCollection(sel);
        if (col.size() == 1) {
            return col.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Returns the unique user with the given id.
     *
     * @param id user's id.
     * @return the unique user with the given id.
     * @throws SQLException if the query failed.
     */
    static UserDto findById(int id) throws DBException {
        UserSpecification sel = new UserSpecification(id);
        Collection<UserDto> col = UserDB.getCollection(sel);
        if (col.size() == 1) {
            return col.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Returns a list of users with the given specifications.
     *
     * @param sel specifications (where clause)
     * @return a list of users with the given specifications.
     * @throws BusinessException if the query failed.
     */
    static List<UserDto> findBySel(UserSpecification sel) throws DBException {
        List<UserDto> col = UserDB.getCollection(sel);
        return col;
    }

    /**
     * Returns a list of all users.
     *
     * @return a list of all users.
     * @throws BusinessException if the query failed.
     */
    static List<UserDto> findAll() throws DBException {
        UserSpecification sel = new UserSpecification(0);
        List<UserDto> col = UserDB.getCollection(sel);
        return col;
    }

    //ajouter les methodes avec une logique métier
}
