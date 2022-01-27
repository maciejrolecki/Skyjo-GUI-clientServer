package skyjo.business;

import skyjo.db.DataUserDB;
import skyjo.dbdto.DataUserDto;
import skyjo.dbspecification.DataUserSpecification;
import skyjo.exception.DBException;
import skyjo.exception.DtoException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Data user business logic.
 *
 * @author mad8
 */
public class DataUserBusinessLogic {

    /**
     * Returns the unique user with the given id.
     *
     * @param id user's id.
     * @return the unique user with the given id.
     * @throws DBException the db exception
     */
    static DataUserDto findById(int id) throws DBException {
        DataUserSpecification sel = new DataUserSpecification(id);
        Collection<DataUserDto> col = DataUserDB.getCollection(sel);
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
     * @throws DBException the db exception
     */
    static List<DataUserDto> findBySel(DataUserSpecification sel) throws DBException {
        List<DataUserDto> col = DataUserDB.getCollection(sel);
        return col;
    }

    /**
     * Returns a list of all users.
     *
     * @return a list of all users.
     * @throws DBException the db exception
     */
    static List<DataUserDto> findAll() throws DBException {
        DataUserSpecification sel = new DataUserSpecification(0);
        List<DataUserDto> col = DataUserDB.getCollection(sel);
        return col;
    }

    /**
     * Update user stat.
     *
     * @param idUser the id user
     * @param score  the score
     * @throws DBException the db exception
     */
    static void updateUserStat(int idUser,int score) throws DBException{
        DataUserSpecification sel = new DataUserSpecification(idUser);
        List<DataUserDto> col = DataUserDB.getCollection(sel);
        switch (col.size()) {
            case 1 -> {
                DataUserDto a = col.get(0);
                DataUserDB.updatePlayedGames(idUser,score);
            }
            case 0 -> {
                try {
                    DataUserDB.insertDb(new DataUserDto(idUser, 1, score, 0));
                } catch (DtoException ex) {
                    Logger.getLogger(DataUserBusinessLogic.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            default -> System.out.println("too many players but not supposed to be the case");
        }
    }

    /**
     * Add win.
     *
     * @param idUser the id user
     * @throws DBException the db exception
     */
    static void addWin(int idUser) throws DBException{
        DataUserSpecification sel = new DataUserSpecification(idUser);
        List<DataUserDto> col = DataUserDB.getCollection(sel);
        if (col.size() == 1) {
            DataUserDto a = col.get(0);
          DataUserDB.updateWinPlayer(a.getIdUser());
        }else{
            System.out.println("too many or missing players but not supposed to be the case");
        }
    }

    /**
     * Add int.
     *
     * @param idUser the id user
     * @return the int
     * @throws DBException the db exception
     */
    static int add(int idUser) throws DBException {
        try {
            return DataUserDB.insertDb(new DataUserDto(idUser,0,99,0));
        } catch (DtoException ex) {
            System.out.println(ex.getMessage());        }
        return -1;
    }
    //ajouter les methodes avec une logique métier
}
