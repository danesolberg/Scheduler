/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.UserDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dane
 */
public class MySqlUserDao implements UserDao {
    @Override
    public UserDto getById(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new UserDto(
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getString("password"),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by")
                );
            }  
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
