/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dane
 */
public class MySqlDaoFactory extends DaoFactory {
    private static final Properties prop = new Properties();
    private static Connection conn = null;
     
    static{
        InputStream input;
        try {
            input = MySqlDaoFactory.class.getResourceAsStream("/config.properties");
            prop.load(input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MySqlDaoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Connection createConnection() throws ClassNotFoundException, SQLException {
        if (conn != null && conn.isValid(1)) {
            return conn;
        }
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://" + prop.getProperty("db.mysql.host") + ":" + prop.getProperty("db.mysql.port") + "/" + prop.getProperty("db.mysql.database");
        
        conn = DriverManager.getConnection(url, prop.getProperty("db.mysql.user"), prop.getProperty("db.mysql.password"));
        
        return conn;
    }
    
    @Override
    public AppointmentDao getAppointmentDao() throws SQLException {
        return new MySqlAppointmentDao();
    }
    
    @Override
    public ContactDao getContactDao() throws SQLException {
        return new MySqlContactDao();
    }
    
    @Override
    public CountryDao getCountryDao() throws SQLException {
        return new MySqlCountryDao();
    }
    
    @Override
    public CustomerDao getCustomerDao() throws SQLException {
        return new MySqlCustomerDao();
    }
    
    @Override
    public DivisionDao getDivisionDao() throws SQLException {
        return new MySqlDivisionDao();
    }
    
    @Override
    public UserDao getUserDao() throws SQLException {
        return new MySqlUserDao();
    }
}
