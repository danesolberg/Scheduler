/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.AppointmentDto;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author dane
 */
public interface AppointmentDao {

    /**
     * Inserts an appointment into the repository
     * @param appointment
     * @return the id of the created appointment
     */
    int create(AppointmentDto appointment);
    
    /**
     * Queries the repository for an appointment by id
     * @param id
     * @return the matching appointment if found
     */
    AppointmentDto getById(int id);
    
    /**
     * Queries the repository for appointments associated to a user
     * @param userId
     * @param start
     * @param end
     * @return a list of matching appointments
     */
    List<AppointmentDto> getByUser(int userId, LocalDateTime start, LocalDateTime end);
    
    /**
     * Queries the repository for appointments associated to a contact
     * @param contactId
     * @return a list of matching appointments
     */
    List<AppointmentDto> getByContact(int contactId);
    
    /**
     * Queries the repository for appointments associated to a user and 
     * starting within a time window
     * @param userId
     * @param startBegin
     * @param startEnd
     * @return a list of matching appointments
     */
    List<AppointmentDto> getUpcoming(int userId, LocalDateTime startBegin, LocalDateTime startEnd);
    
    /**
     * Updates an appointment in the repository with new data
     * @param appointment
     * @return whether or not the update was successful
     */
    boolean update(AppointmentDto appointment);
    
    /**
     * Deletes an appointment from the repository
     * @param appointment
     * @return whether or not the delete was successful
     */
    boolean delete(AppointmentDto appointment);
    
    /**
     * Deletes all appointments associated with a customer
     * @param customerId
     * @return the number of deleted appointments
     */
    int deleteByCustomer(int customerId);
    
    /**
     * Tests whether an appointment overlaps with other appointments
     * @param appointment
     * @return whether or not the time window is available
     */
    boolean isTimeAvailable(AppointmentDto appointment);
    
    /**
     * Calculates the number of appointments per month by type
     * @return the query results
     */
    ResultSet aggregateByTypeMonth();
    
    /**
     * Calculates the number of appointments by customer
     * @return the query results
     */
    ResultSet aggregateByCustomer();
}
