/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.App;
import dto.CustomerDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Customer;

/**
 *
 * @author dane
 */
public class CustomerService {

    /**
     *
     */
    private final CustomerMapper mapper = new CustomerMapper();
    
    /**
     *
     * @param customer
     * @return
     * @throws SQLException
     */
    public int create(Customer customer) throws SQLException {
        CustomerDto dto = mapper.toDto(customer);
        return App.getDaoFactory().getCustomerDao().create(dto);
    }
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public List<Customer> getAll() throws SQLException {
        List<CustomerDto> dtos = App.getDaoFactory().getCustomerDao().getAll();
        List<Customer> domains = new ArrayList<>();
        
        
        for (CustomerDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    }
    
    /**
     *
     * @param customer
     * @return
     * @throws SQLException
     */
    public boolean update(Customer customer) throws SQLException {
        CustomerDto dto = mapper.toDto(customer);
        return App.getDaoFactory().getCustomerDao().update(dto);
    }
    
    /**
     *
     * @param customer
     * @return
     * @throws SQLException
     */
    public boolean delete(Customer customer) throws SQLException {
        CustomerDto dto = mapper.toDto(customer);
        int customerId = customer.getCustomerId();
        
        //TODO refactor to set autocommit to false and maintain access to individual
        //database connections to commit all individual transactions only upon
        //success completion of all sql statements.
        AppointmentService appointmentService = ServiceFactory.getAppointmentInstance();
        appointmentService.deleteByCustomer(customerId);
        return App.getDaoFactory().getCustomerDao().delete(dto);
    }
}
