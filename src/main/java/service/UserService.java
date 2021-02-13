/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.App;
import dto.UserDto;
import java.sql.SQLException;
import model.User;

/**
 *
 * @author dane
 */
public class UserService {

    /**
     *
     */
    private UserMapper mapper = new UserMapper();
    
    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public User getById(int id) throws SQLException {
        UserDto userDto = App.getDaoFactory().getUserDao().getById(id);
        if (userDto != null) {
            return mapper.toDomain(userDto, App.getDaoFactory());
        }
        return null;
    }
}
