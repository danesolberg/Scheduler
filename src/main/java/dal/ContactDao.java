/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.ContactDto;
import java.util.List;

/**
 *
 * @author dane
 */
public interface ContactDao {

    /**
     * Queries the repository for a contact by id
     * @param id
     * @return return the matching contact if one exists
     */
    ContactDto getById(int id);
    
    /**
     * Queries the repository for all contacts
     * @return a list of all contacts
     */
    List<ContactDto> getAll();
}
