/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.CustomerDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dane
 */
public class MySqlCustomerDao implements CustomerDao {

    @Override
    public int create(CustomerDto customer) {
        String query = "INSERT INTO customers " +
            "(customer_name, address, postal_code, phone, create_date, " +
            "created_by, last_update, last_updated_by, division_id) " +
            "VALUES (?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int key = -1;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostalCode());
            stmt.setString(4, customer.getPhone());
            stmt.setTimestamp(5, Timestamp.valueOf(customer.getCreateDate()));
            stmt.setString(6, customer.getCreatedBy());
            stmt.setTimestamp(7, Timestamp.valueOf(customer.getLastUpdate()));
            stmt.setString(8, customer.getLastUpdatedBy());
            stmt.setInt(9, customer.getDivisionId());
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
    public CustomerDto getById(int id) {
        String query = "SELECT * FROM customers WHERE customer_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new CustomerDto(
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("address"),
                    rs.getString("postal_code"),
                    rs.getString("phone"),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("division_id")
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
    public boolean update(CustomerDto customer) {
        //TODO add function to DTO that returns only modified fields, then write
        //update statement to only consider modified fields.
        String query = "UPDATE customers SET " +
            "customer_name=?, address=?, postal_code=?, phone=?," +
            "last_update=?, last_updated_by=?, division_id=? WHERE customer_id=?";
        Connection conn = null;
        PreparedStatement stmt = null;
        int updated = 0;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setString(1, customer.getCustomerName());
            stmt.setString(2, customer.getAddress());
            stmt.setString(3, customer.getPostalCode());
            stmt.setString(4, customer.getPhone());
            stmt.setTimestamp(5, Timestamp.valueOf(customer.getLastUpdate()));
            stmt.setString(6, customer.getLastUpdatedBy());
            stmt.setInt(7, customer.getDivisionId());
            stmt.setInt(8, customer.getCustomerId());
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
    public boolean delete(CustomerDto customer) {
        String query = "DELETE FROM customers WHERE customer_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        int deleted = 0;

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, customer.getCustomerId());
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
    public List<CustomerDto> getAll() {
        String query = "SELECT * FROM customers ORDER BY customer_name";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<CustomerDto> retVal = new ArrayList<>();

        try {
            conn = MySqlDaoFactory.createConnection();
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                retVal.add(new CustomerDto(
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("address"),
                    rs.getString("postal_code"),
                    rs.getString("phone"),
                    rs.getTimestamp("create_date").toLocalDateTime(),
                    rs.getString("created_by"),
                    rs.getTimestamp("last_update").toLocalDateTime(),
                    rs.getString("last_updated_by"),
                    rs.getInt("division_id")
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
