/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import dto.CustomerDto;
import java.sql.SQLException;
import model.Customer;
import model.Division;

/**
 *
 * @author dane
 */
public class CustomerMapper implements Mapper<CustomerDto, Customer> {

    @Override
    public CustomerDto toDto(Customer customer) {
        return new CustomerDto(
            customer.getCustomerId(),
            customer.getCustomerName(),
            customer.getAddress(),
            customer.getPostalCode(),
            customer.getPhone(),
            customer.getCreateDate(),
            customer.getCreatedBy(),
            customer.getLastUpdate(),
            customer.getLastUpdatedBy(),
            customer.getDivision().getDivisionId()
        );
    }

    @Override
    public Customer toDomain(CustomerDto customerDto, DaoFactory daoFactory) throws SQLException {
        DivisionMapper mapper = new DivisionMapper();
        Division division = mapper.toDomain(daoFactory.getDivisionDao().getById(customerDto.getDivisionId()), daoFactory);
        
        return new Customer(
            customerDto.getCustomerId(),
            customerDto.getCustomerName(),
            customerDto.getAddress(),
            customerDto.getPostalCode(),
            customerDto.getPhone(),
            customerDto.getCreateDate(),
            customerDto.getCreatedBy(),
            customerDto.getLastUpdate(),
            customerDto.getLastUpdatedBy(),
            division
        );
    }
    
}
