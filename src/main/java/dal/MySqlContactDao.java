/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.ContactDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dane
 */
public class MySqlContactDao implements ContactDao {

    @Override
    public ContactDto getById(int id) {
        String query = "SELECT * FROM contacts WHERE contact_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new ContactDto(
                    rs.getInt("contact_id"),
                    rs.getString("contact_name"),
                    rs.getString("email")
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

    @Override
    public List<ContactDto> getAll() {
       String query = "SELECT * FROM contacts ORDER BY contact_name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<ContactDto> retVal = new ArrayList<>();

        try {
            conn = MySqlDaoFactory.createConnection();      
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                retVal.add(new ContactDto(
                    rs.getInt("contact_id"),
                    rs.getString("contact_name"),
                    rs.getString("email")
                ));
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
        return retVal;
    }
    
}
