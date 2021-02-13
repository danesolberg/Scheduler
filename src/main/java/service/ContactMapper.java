/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import dto.ContactDto;
import model.Contact;

/**
 *
 * @author dane
 */
public class ContactMapper implements Mapper<ContactDto, Contact> {

    @Override
    public ContactDto toDto(Contact contact) {
        return new ContactDto(
            contact.getContactId(),
            contact.getContactName(),
            contact.getEmail()
        );
    }

    @Override
    public Contact toDomain(ContactDto contactDto, DaoFactory daoFactory) {
        return new Contact(
            contactDto.getContactId(),
            contactDto.getContactName(),
            contactDto.getEmail()
        );
    }
    
}
