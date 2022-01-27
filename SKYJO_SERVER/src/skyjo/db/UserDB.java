package skyjo.db;

import skyjo.dbdto.UserDto;
import skyjo.exception.DBException;
import skyjo.exception.DtoException;
import skyjo.dbspecification.UserSpecification;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mad8
 */
public class UserDB {

    private static final String recordName = "USER";

    /**
     *
     * @return @throws skyjo.exception.DtoException
     * @throws skyjo.exception.DBException
     */
    public static List<UserDto> getAllUsers() throws DtoException, DBException {
        List<UserDto> users = getCollection(new UserSpecification(0));
        return users;
    }

    /**
     *
     * @param sel
     * @return
     * @throws skyjo.exception.DBException
     */
    public static List<UserDto> getCollection(UserSpecification sel) throws DBException {
        List<UserDto> al = new ArrayList<>();
        try {
            String query = "Select (\"id\"),(\"mail\") FROM \"main\".\"USERS\"";
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement stmt;
            String where = "";
            if (sel.getId() != 0) {
                where = where + " (\"id\") = ? ";
            }
            if (sel.getMail() != null && !sel.getMail().isEmpty()) {
                if (!where.isEmpty()) {
                    where = where + " AND ";
                }
                where = where + " (\"mail\") like ? ";
            }

            if (where.length() != 0) {
                where = " where " + where + " order by (\"mail\")";
                query = query + where;
                stmt = connexion.prepareStatement(query);
                int i = 1;
                if (sel.getId() != 0) {
                    stmt.setInt(i, sel.getId());
                    i++;

                }
                if (sel.getMail() != null && !sel.getMail().isEmpty()) {
                    stmt.setString(i, sel.getMail() + "%");
                    i++;
                }
            } else {
                query = query + " Order by (\"mail\")";
                stmt = connexion.prepareStatement(query);
            }
            java.sql.ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                al.add(new UserDto(rs.getInt("id"), rs.getString("mail")));
            }
        } catch (DtoException ex) {
            throw new DBException("Instanciation de " + recordName + " impossible:\nDTOException: " + ex.getMessage());
        } catch (java.sql.SQLException eSQL) {
            throw new DBException("Instanciation de " + recordName + " impossible:\nSQLException: " + eSQL.getMessage());
        }
        return al;
    }

    /**
     *
     * @param id
     * @throws skyjo.exception.DBException
     */
    public static void deleteDb(int id) throws DBException {
        try {
            java.sql.Statement stmt = DBManager.getConnection().createStatement();
            stmt.execute("delete from \"main\".\"USERS\" where (\"id\")=" + id);
        } catch (Exception ex) {
            throw new DBException(recordName + ": suppression impossible\n" + ex.getMessage());
        }
    }

    /**
     *
     * @param record
     * @throws skyjo.exception.DBException
     */
    public static void updateDb(UserDto record) throws DBException {
        try {
            java.sql.Connection connexion = DBManager.getConnection();

            java.sql.PreparedStatement update;
            String sql = "Update \"main\".\"USERS\" set "
                    + "(\"MAIL\")=?, "
                    + "where (\"id\")=?";
            update = connexion.prepareStatement(sql);
            update.setString(1, record.getMail());
            update.setInt(2, record.getId());
            update.executeUpdate();
        } catch (DBException | SQLException ex) {
            throw new DBException(recordName + ", modification impossible:\n" + ex.getMessage());
        }
    }

    /**
     *
     * @param  userMail
     * @return
     * @throws skyjo.exception.DBException
     */
    public static int insertDb(String userMail) throws DBException {
        try {
            //int num = SequenceDB.getNextNum(SequenceDB.USERS);
            java.sql.Connection connexion = DBManager.getConnection();
            java.sql.PreparedStatement insert;
            insert = connexion.prepareStatement("INSERT INTO main.USERS (MAIL) VALUES (?)");
            insert.setString(1, userMail);
            insert.executeUpdate();
            List<UserDto> users = getCollection(new UserSpecification(userMail));
            if(users.size()==1)return users.get(0).getId();
            else return 0;
        } catch (DBException | SQLException ex) {
            throw new DBException(recordName + ": ajout impossible\n" + ex.getMessage());
        }
    }

    //faire une methode sur un attribut particulier un boolean active par exemple
}
