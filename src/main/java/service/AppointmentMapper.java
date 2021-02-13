/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import dto.AppointmentDto;
import java.sql.SQLException;
import model.Appointment;
import model.Contact;
import model.Customer;
import model.User;

/**
 *
 * @author dane
 */
public class AppointmentMapper implements Mapper<AppointmentDto, Appointment> {

    @Override
    public AppointmentDto toDto(Appointment appointment) {
        return new AppointmentDto(
            appointment.getAppointmentId(),
            appointment.getTitle(),
            appointment.getDescription(),
            appointment.getLocation(),
            appointment.getType(),
            appointment.getStart(),
            appointment.getEnd(),
            appointment.getCreateDate(),
            appointment.getCreatedBy(),
            appointment.getLastUpdate(),
            appointment.getLastUpdatedBy(),
            appointment.getCustomer().getCustomerId(),
            appointment.getUser().getUserId(),
            appointment.getContact().getContactId()
        );
    }

    @Override
    public Appointment toDomain(AppointmentDto appointmentDto, DaoFactory daoFactory) throws SQLException {       
        CustomerMapper customerMapper = new CustomerMapper();
        UserMapper userMapper = new UserMapper();
        ContactMapper contactMapper = new ContactMapper();
        
        Customer customer = customerMapper.toDomain(daoFactory.getCustomerDao().getById(appointmentDto.getCustomerId()), daoFactory);
        User user = userMapper.toDomain(daoFactory.getUserDao().getById(appointmentDto.getUserId()), daoFactory);
        Contact contact = contactMapper.toDomain(daoFactory.getContactDao().getById(appointmentDto.getContactId()), daoFactory);
        
        return new Appointment(
            appointmentDto.getAppointmentId(),
            appointmentDto.getTitle(),
            appointmentDto.getDescription(),
            appointmentDto.getLocation(),
            appointmentDto.getType(),
            appointmentDto.getStart(),
            appointmentDto.getEnd(),
            appointmentDto.getCreateDate(),
            appointmentDto.getCreatedBy(),
            appointmentDto.getLastUpdate(),
            appointmentDto.getLastUpdatedBy(),
            customer,
            user,
            contact
        );
    }
    
}
