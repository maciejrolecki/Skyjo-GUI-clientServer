/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package skyjo.business;

import skyjo.dbdto.UserDto;
import skyjo.exception.DBBusinessException;
import java.util.List;

/**
 *
 * @author mad8
 */
public interface AdminFacade {
    /**
     * Returns a list of all users.
     *
     * @return a list of all users.
     * @throws DBBusinessException if the query failed.
     */
    List<UserDto> getUsers() throws DBBusinessException;

    /**
     * Returns the unique user with the given id.
     *
     * @param id user's id.
     * @return the unique user with the given id.
     * @throws skyjo.exception.DBBusinessException
     */
    UserDto getUser(int id) throws DBBusinessException;

    /**
     * Returns the last user with the given name.
     *
     * @param mail user's mail.
     * @return the last user with the given name.
     * @throws skyjo.exception.DBBusinessException
     */
    UserDto getUser(String mail) throws DBBusinessException;

    /**
     * Creates a user and insert it in the database.Returns the user's id.
     *
     * @param mail user's mail.
     * @return the user's id.
     * @throws skyjo.exception.DBBusinessException
     */
    int addUser(String userMail) throws DBBusinessException;

    /**
     * Updates the given user.
     *
     * @param current
     * @throws skyjo.exception.DBBusinessException
     */
    void updateUser(UserDto current) throws DBBusinessException;
}
