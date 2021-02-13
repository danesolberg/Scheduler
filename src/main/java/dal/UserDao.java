/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.UserDto;

/**
 *
 * @author dane
 */
public interface UserDao {

    /**
     *
     * @param id
     * @return
     */
    UserDto getById(int id);
}
