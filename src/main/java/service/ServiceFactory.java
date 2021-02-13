/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

/**
 *
 * @author dane
 */
public class ServiceFactory {

    /**
     *
     */
    private static AppointmentService appointment = null;

    /**
     *
     */
    private static ContactService contact = null;

    /**
     *
     */
    private static CountryService country = null;

    /**
     *
     */
    private static CustomerService customer = null;

    /**
     *
     */
    private static DivisionService division = null;

    /**
     *
     */
    private static UserService user = null;
    
    /**
     *
     * @return
     */
    public static AppointmentService getAppointmentInstance() {
        if (appointment == null) {
            synchronized(ServiceFactory.class) {
                appointment = new AppointmentService();
            }
        }
        return appointment;
    }
    
    /**
     *
     * @return
     */
    public static ContactService getContactInstance() {
        if (contact == null) {
            synchronized(ServiceFactory.class) {
                contact = new ContactService();
            }
        }
        return contact;
    }
    
    /**
     *
     * @return
     */
    public static CountryService getCountryInstance() {
        if (country == null) {
            synchronized(ServiceFactory.class) {
                country = new CountryService();
            }
        }
        return country;
    }
    
    /**
     *
     * @return
     */
    public static CustomerService getCustomerInstance() {
        if (customer == null) {
            synchronized(ServiceFactory.class) {
                customer = new CustomerService();
            }
        }
        return customer;
    }
    
    /**
     *
     * @return
     */
    public static DivisionService getDivisionInstance() {
        if (division == null) {
            synchronized(ServiceFactory.class) {
                division = new DivisionService();
            }
        }
        return division;
    }
    
    /**
     *
     * @return
     */
    public static UserService getUserInstance() {
        if (user == null) {
            synchronized(ServiceFactory.class) {
                user = new UserService();
            }
        }
        return user;
    }
}
