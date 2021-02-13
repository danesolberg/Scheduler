/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javafx.util.StringConverter;
import model.Contact;
import model.Country;
import model.Customer;
import model.Division;

/**
 * Provides methods for displaying object members within JavaFX combo boxes
 * @author dane
 */
public class ModelConverter {

    /**
     * Displays a Country object's country name
     */
    public static StringConverter<Country> countryConverter = new StringConverter<Country>() {
        @Override
        public String toString(Country country) {
            if (country == null) { return null; }
            return country.getCountry();
        }
        
        @Override
        public Country fromString(String string) {
           return null;
        }
    };
    
    /**
     * Displays a Division object's division name
     */
    public static StringConverter<Division> divisionConverter = new StringConverter<Division>() {
        @Override
        public String toString(Division division) {
            if (division == null) { return null; }
            return division.getDivision();
        }
        
        @Override
        public Division fromString(String string) {
           return null;
        }
    };
    
    /**
     * Displays a Customer object's customer name
     */
    public static StringConverter<Customer> customerConverter = new StringConverter<Customer>() {
        @Override
        public String toString(Customer customer) {
            if (customer == null) { return null; }
            return customer.getCustomerName();
        }
        
        @Override
        public Customer fromString(String string) {
           return null;
        }
    };
    
    /**
     * Displays a Contact object's contact name
     */
    public static StringConverter<Contact> contactConverter = new StringConverter<Contact>() {
        @Override
        public String toString(Contact contact) {
            if (contact == null) { return null; }
            return contact.getContactName();
        }
        
        @Override
        public Contact fromString(String string) {
           return null;
        }
    };
}
