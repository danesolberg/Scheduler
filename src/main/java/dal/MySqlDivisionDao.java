/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.DivisionDto;
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
public class MySqlDivisionDao implements DivisionDao {

    @Override
    public DivisionDto getById(int id) {
        String query = "SELECT * FROM first_level_divisions WHERE division_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new DivisionDto(
                    rs.getInt("division_id"),
                    rs.getString("division"),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("country_id")
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
    public List<DivisionDto> getAll() {
       String query = "SELECT * FROM first_level_divisions ORDER BY division";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<DivisionDto> retVal = new ArrayList<>();

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                retVal.add(new DivisionDto(
                    rs.getInt("division_id"),
                    rs.getString("division"),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("country_id")
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
