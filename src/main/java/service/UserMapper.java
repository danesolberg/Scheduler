/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import dto.UserDto;
import model.User;

/**
 *
 * @author dane
 */
public class UserMapper implements Mapper<UserDto, User> {
    @Override
    public UserDto toDto(User user) {
        return new UserDto(
            user.getUserId(),
            user.getUserName(),
            user.getPassword(),
            user.getCreateDate(),
            user.getCreatedBy(),
            user.getLastUpdate(),
            user.getLastUpdatedBy()
        );
    }
    
    @Override
    public User toDomain(UserDto userDto, DaoFactory daoFactory) {
        return new User(
            userDto.getUserId(),
            userDto.getUserName(),
            userDto.getPassword(),
            userDto.getCreateDate(),
            userDto.getCreatedBy(),
            userDto.getLastUpdate(),
            userDto.getLastUpdatedBy()
        );
    }
}
