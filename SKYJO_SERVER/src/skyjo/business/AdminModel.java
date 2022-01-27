package skyjo.business;

import skyjo.db.DBManager;
import skyjo.db.SequenceDB;
import skyjo.dbdto.DataGameDto;
import skyjo.dbdto.DataUserDto;
import skyjo.dbdto.UserDto;
import skyjo.exception.DBBusinessException;
import skyjo.exception.DBException;
import skyjo.exception.DtoException;
import skyjo.dbspecification.UserSpecification;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.swing.tree.ExpandVetoException;

/**
 * The type Admin model.
 *
 * @author mad8
 */
public class AdminModel implements AdminFacade {
    // private static final String URL = "jdbc:derby://localhost:1527/threeLayer";
    // private static final String USER_DB = "app";
    // private static final String PASSWORD = "app";

    // static final String USERS = "USERS";
    // static int getNextNum(String sequence) throws SQLException {
    // Connection connexion = DriverManager.getConnection(URL, USER_DB, PASSWORD);
    // String query = "Update SEQUENCES set sValue = sValue+1 where id='" + sequence + "'";
    // java.sql.PreparedStatement update = connexion.prepareStatement(query);
    // update.execute();
    // java.sql.Statement stmt = connexion.createStatement();
    // query = "Select sValue FROM Sequences where id='" + sequence + "'";
    // java.sql.ResultSet rs = stmt.executeQuery(query);
    // int nvId;
    // if (rs.next()) {
    // nvId = rs.getInt("sValue");
    // return nvId;
    // } else {
    // throw new IllegalStateException("Nouveau n° de séquence inaccessible!");
    // }
    // }
    @Override
    public List<UserDto> getUsers() throws DBBusinessException {
        try {
            DBManager.startTransaction();
            List<UserDto> col = UserBusinessLogic.findAll();
            DBManager.validateTransaction();
            return col;
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Liste des Users inaccessible! \n" + msg);
            }
        }
    }

    /**
     *
     * @param userId
     * @return user
     * @throws DBBusinessException
     */
    @Override
    public UserDto getUser(int userId) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            UserDto user = UserBusinessLogic.findById(userId);
            DBManager.validateTransaction();
            return user;
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Liste des Users getUser inaccessible! \n" + msg);
            }
        }
    }

    /**
     *
     * @param userMail
     * @return user
     * @throws DBBusinessException
     */
    @Override
    public UserDto getUser(String userMail) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            UserDto user = UserBusinessLogic.findByMail(userMail);
            return user;
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Liste des Users    inaccessible! \n" + msg);
            }
        }
    }

    /**
     * Gets user id.
     *
     * @param userMail the user mail
     * @return the user id
     * @throws DBBusinessException the db business exception
     */
    public int getUserId(String userMail) throws DBBusinessException {
        UserDto ret = this.getUser(userMail);
        if (ret == null) {
            return addUser(userMail);
        }
        return ret.getId();
    }

    /**
     * Gets selected users.
     *
     * @param sel the sel
     * @return the selected users
     * @throws DBBusinessException the db business exception
     */
    public static Collection<UserDto> getSelectedUsers(UserSpecification sel) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            Collection<UserDto> col = UserBusinessLogic.findBySel(sel);
            DBManager.validateTransaction();
            return col;
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Liste des Users get user inaccessible! \n" + msg);
            }
        }
    }

    /**
     *
     * @param userMail
     * @return an int
     * @throws DBBusinessException
     */
    @Override
    public int addUser(String userMail) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            int b = UserBusinessLogic.add(userMail);
            System.out.println(b);
            int c = DataUserBusinessLogic.add(b);
            System.out.println(c);
            DBManager.validateTransaction();
            return b;
        } catch (DBException ex) {
            try {
                DBManager.cancelTransaction();
                throw new DBBusinessException(ex.getMessage());
            } catch (DBException ex1) {
                throw new DBBusinessException(ex1.getMessage());
            }
        }
    }

    /**
     * 
     * @param user
     * @throws DBBusinessException
     */
    @Override
    public void updateUser(UserDto user) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            UserBusinessLogic.update(user);
            DBManager.validateTransaction();
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Mise à jour de User impossible! \n" + msg);
            }
        }
    }

    /**
     * Update user scores.
     *
     * @param usersId   the users id
     * @param userScore the user score
     * @throws DBException         the db exception
     * @throws DBBusinessException the db business exception
     */
    public void updateUserScores(List<Integer> usersId, List<Integer> userScore) throws DBException, DBBusinessException {
        try {
            DBManager.startTransaction();
            for (int i = 0; i < usersId.size(); i++) {
                DataUserBusinessLogic.updateUserStat(usersId.get(i), userScore.get(i));
            }
            DBManager.validateTransaction();
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Mise à jour des résultats des utilisateurs impossible! \n" + msg);
            }
        }
    }

    /**
     * Add win.
     *
     * @param Userid the userid
     * @throws DBBusinessException the db business exception
     */
    public void addWin(int Userid) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            DataUserBusinessLogic.addWin(Userid);
            DBManager.validateTransaction();
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Mise à jour des résultats des winner des gains impossible! \n" + msg);
            }
        }
    }

    /**
     * Add data from game.
     *
     * @param usersId    the users id
     * @param usersScore the users score
     * @param srvID      the srv id
     * @throws DBBusinessException the db business exception
     */
    public void addDataFromGame(List<Integer> usersId, List<Integer> usersScore, int srvID) throws DBBusinessException {
        try {
            DBManager.startTransaction();
            for (int i = 0; i < usersId.size(); i++) {
                try {
                    DataGameBusinessLogic.add(new DataGameDto(srvID, usersId.get(i), usersScore.get(i)));
                } catch (DtoException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            DBManager.validateTransaction();
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
            } finally {
                throw new DBBusinessException("Mise à jour des résultats impossible! \n" + msg);
            }
        }
    }

    /**
     * Gets next num.
     *
     * @return the next num
     */
    public static int getNextNum() {
        int ret = 0;
        try {
            ret = SequenceDB.getNextNum("GAMEID");
        } catch (DBException e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    /**
     * Gets user stats.
     *
     * @param idUser the id user
     * @return List
     * @throws DBBusinessException the db business exception
     */
    public List<Integer> getUserStats(int idUser) throws DBBusinessException {
        List<Integer> a = new ArrayList();
        try {
            DBManager.startTransaction();
            DataUserDto user = DataUserBusinessLogic.findById(idUser);
            DBManager.validateTransaction();
            a.add(0, user.getNbrWins());
            a.add(1, user.getNbrGames());
            a.add(2, user.getLowestScore());
            return a;
        } catch (DBException eDB) {
            String msg = eDB.getMessage();
            try {
                DBManager.cancelTransaction();
            } catch (DBException ex) {
                msg = ex.getMessage() + "\n" + msg;
                System.out.println(msg);
            }
        }
        return null;
    }
}
