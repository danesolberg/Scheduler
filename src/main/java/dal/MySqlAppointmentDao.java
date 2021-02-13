/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.AppointmentDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dane
 */
public class MySqlAppointmentDao implements AppointmentDao {

    @Override
    public int create(AppointmentDto appointment) {
        String query = "INSERT INTO appointments " +
            "(title, description, location, type, start, end, create_date," +
            "created_by, last_update, last_updated_by, customer_id," +
            "user_id, contact_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int key = -1;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            stmt.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            stmt.setTimestamp(7, Timestamp.valueOf(appointment.getCreateDate()));
            stmt.setString(8, appointment.getCreatedBy());
            stmt.setTimestamp(9, Timestamp.valueOf(appointment.getLastUpdate()));
            stmt.setString(10, appointment.getLastUpdatedBy());
            stmt.setInt(11, appointment.getCustomerId());
            stmt.setInt(12, appointment.getUserId());
            stmt.setInt(13, appointment.getContactId());
            stmt.executeUpdate();  
            
            rs = stmt.getGeneratedKeys();
            
            if (rs.next()) {
                key = rs.getInt(1);
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
        return key;
    }

    @Override
    public AppointmentDto getById(int id) {
        String query = "SELECT * FROM appointments WHERE appointment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new AppointmentDto(
                    rs.getInt("appointment_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getTimestamp("start").toLocalDateTime(),
                    rs.getTimestamp("end").toLocalDateTime(),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("customer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id")
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
    public boolean update(AppointmentDto appointment) {
        //TODO add function to DTO that returns only modified fields, then write
        //update statement to only consider modified fields.
        String query = "UPDATE appointments SET " +
            "title=?, description=?, location=?, type=?, start=?, end=?," +
            "last_update=?, last_updated_by=?, customer_id=?," +
            "contact_id=? WHERE appointment_id=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        int updated = 0;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setString(1, appointment.getTitle());
            stmt.setString(2, appointment.getDescription());
            stmt.setString(3, appointment.getLocation());
            stmt.setString(4, appointment.getType());
            stmt.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            stmt.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            stmt.setTimestamp(7, Timestamp.valueOf(appointment.getLastUpdate()));
            stmt.setString(8, appointment.getLastUpdatedBy());
            stmt.setInt(9, appointment.getCustomerId());
            stmt.setInt(10, appointment.getContactId());
            stmt.setInt(11, appointment.getAppointmentId());
            updated = stmt.executeUpdate();  
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return updated == 1;
    }

    @Override
    public boolean delete(AppointmentDto appointment) {
        String query = "DELETE FROM appointments WHERE appointment_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        int deleted = 0;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, appointment.getAppointmentId());
            deleted = stmt.executeUpdate();
            

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return deleted == 1;
    }

    @Override
    public List<AppointmentDto> getByUser(int userId, LocalDateTime start, LocalDateTime end) {
        String query = "SELECT * FROM appointments WHERE user_id = ? and start >= ? and end <= ? ORDER BY start";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppointmentDto> retVal = new ArrayList<>();

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(start));
            stmt.setTimestamp(3, Timestamp.valueOf(end));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                retVal.add(new AppointmentDto(
                    rs.getInt("appointment_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getTimestamp("start").toLocalDateTime(),
                    rs.getTimestamp("end").toLocalDateTime(),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("customer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id")
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
    
    @Override
    public List<AppointmentDto> getByContact(int contactId) {
        String query = "SELECT * FROM appointments WHERE contact_id = ? ORDER BY start";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppointmentDto> retVal = new ArrayList<>();

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, contactId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                retVal.add(new AppointmentDto(
                    rs.getInt("appointment_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getTimestamp("start").toLocalDateTime(),
                    rs.getTimestamp("end").toLocalDateTime(),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("customer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id")
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
    
    @Override
    public List<AppointmentDto> getUpcoming(int userId, LocalDateTime startBegin, LocalDateTime startEnd) {
        String query = "SELECT * FROM appointments WHERE user_id = ? and start >= ? and start <= ? ORDER BY start";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppointmentDto> retVal = new ArrayList<>();

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setTimestamp(2, Timestamp.valueOf(startBegin));
            stmt.setTimestamp(3, Timestamp.valueOf(startEnd));
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                retVal.add(new AppointmentDto(
                    rs.getInt("appointment_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("location"),
                    rs.getString("type"),
                    rs.getTimestamp("start").toLocalDateTime(),
                    rs.getTimestamp("end").toLocalDateTime(),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("customer_id"),
                    rs.getInt("user_id"),
                    rs.getInt("contact_id")
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
    
    @Override
    public int deleteByCustomer(int customerId) {
        String query = "DELETE FROM appointments WHERE customer_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        int deleted = 0;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, customerId);
            deleted = stmt.executeUpdate();
            

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return deleted;
    }
    
    @Override
    public boolean isTimeAvailable(AppointmentDto appointment) {
        String query = "SELECT appointment_id FROM appointments WHERE customer_id = ? and end > ? and start < ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, appointment.getCustomerId());
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getStart()));
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getEnd()));
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return false;
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
        return true;
    }
    
    @Override
    public ResultSet aggregateByTypeMonth() {
        String query = "SELECT Type, MONTHNAME(start) AS Month, count(1) AS Total FROM appointments GROUP BY type, month ORDER BY type, month";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            return rs;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (conn != null) conn.close();
//            } catch (SQLException ex) {
//                Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
//            }
        }
        return null;
    }
    
    @Override
    public ResultSet aggregateByCustomer() {
        String query = "SELECT created_by AS 'Creator', count(1) AS Total FROM appointments GROUP BY created_by ORDER BY created_by";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            return rs;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MySqlUserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
