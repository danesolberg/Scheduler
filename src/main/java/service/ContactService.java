/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.App;
import dto.ContactDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Contact;

/**
 *
 * @author dane
 */
public class ContactService {

    /**
     *
     */
    private final ContactMapper mapper = new ContactMapper();
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public List<Contact> getAll() throws SQLException {
        List<ContactDto> dtos = App.getDaoFactory().getContactDao().getAll();
        List<Contact> domains = new ArrayList<>();
        
        
        for (ContactDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    }
}
