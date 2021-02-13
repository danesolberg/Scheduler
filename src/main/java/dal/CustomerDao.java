/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.CustomerDto;
import java.util.List;

/**
 *
 * @author dane
 */
public interface CustomerDao {

    /**
     *
     * @param customer
     * @return
     */
    int create(CustomerDto customer);
    
    /**
     *
     * @param id
     * @return
     */
    CustomerDto getById(int id);
    
    /**
     *
     * @return
     */
    List<CustomerDto> getAll();
    
    /**
     *
     * @param appointment
     * @return
     */
    boolean update(CustomerDto appointment);
    
    /**
     *
     * @param appointment
     * @return
     */
    boolean delete(CustomerDto appointment);
}
