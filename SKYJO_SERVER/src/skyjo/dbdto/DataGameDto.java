package skyjo.dbdto;

import skyjo.exception.DtoException;

/**
 * The type Data game dto.
 *
 * @author mad8
 */
public class DataGameDto {
    private Integer idGame;
    private Integer idUser;
    private Integer score;

    /**
     * constructeur d'une partie
     *
     * @param idGame the id game
     * @param idUser the id user
     * @param score  the score
     * @throws DtoException the dto exception
     */
    public DataGameDto(Integer idGame, Integer idUser, Integer score) throws DtoException {
      this.idGame = idGame;
      this.idUser = idUser;
      this.score = score;
    }

    /**
     * Gets id game.
     *
     * @return the id game
     */
    public int getIdGame() {
        return idGame;
    }

    /**
     * Gets id user.
     *
     * @return the id user
     */
    public int getIdUser() {
        return idUser;
    }

    /**
     * Gets score.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets id game.
     *
     * @param idGame the id game
     */
    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    /**
     * Sets id user.
     *
     * @param idJoueur the id joueur
     */
    public void setIdUser(int idJoueur) {
        this.idUser = idJoueur;
    }

    /**
     * Sets score.
     *
     * @param score the score
     */
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "[UserDto] (" + getIdGame() + ") " + getIdUser()+ " " + getScore();
    }
}
