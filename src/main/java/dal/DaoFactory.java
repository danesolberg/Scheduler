/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import java.sql.SQLException;

/**
 * Abstract base class that implements a getDaoFactory method to
 * instantiate the proper factory
 * @author dane
 */
public abstract class DaoFactory {
    public static final int MYSQL = 1;
    
    public abstract AppointmentDao getAppointmentDao() throws SQLException;
    public abstract ContactDao getContactDao() throws SQLException;
    public abstract CountryDao getCountryDao() throws SQLException;
    public abstract CustomerDao getCustomerDao() throws SQLException;
    public abstract DivisionDao getDivisionDao() throws SQLException;
    public abstract UserDao getUserDao() throws SQLException;
    
    /**
     * 
     * @param factoryType
     * @return an instance of the selected DaoFactory implementation
     */
    public static DaoFactory getDaoFactory(int factoryType) {
        switch (factoryType) {
            case MYSQL: 
                return new MySqlDaoFactory();
        }
        throw new IllegalArgumentException("Unsupported DAO factory type passed.");
    }
}
