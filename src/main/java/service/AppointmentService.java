/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.App;
import dto.AppointmentDto;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Appointment;

/**
 *
 * @author dane
 */
public class AppointmentService {

    /**
     * Converts Domain objects to/from DTOs
     */
    private final AppointmentMapper mapper = new AppointmentMapper();

    /**
     * Pulls in user configuration data
     */
    private static final Properties prop = new Properties();
    
    static{
        InputStream input;
        try {
            input = AppointmentService.class.getResourceAsStream("/config.properties");
            prop.load(input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AppointmentService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppointmentService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean endsAfterStart(Appointment appointment) {
        if (appointment.getEnd().isAfter(appointment.getStart())) { return true; }
        return false;
    }
    
    private boolean withinBusinessHours(Appointment appointment) {
        LocalDate appointmentDate = appointment.getStart().toLocalDate();
        ZonedDateTime startEST = appointmentDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(Integer.valueOf(prop.getProperty("business_hours.start")));
        ZonedDateTime endEST = appointmentDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(Integer.valueOf(prop.getProperty("business_hours.end")));
        
        
        if (appointment.getStart().atZone(ZoneId.of("America/New_York")).isBefore(startEST) ||
            appointment.getStart().atZone(ZoneId.of("America/New_York")).isAfter(endEST) ||
            appointment.getEnd().atZone(ZoneId.of("America/New_York")).isBefore(startEST) ||
            appointment.getEnd().atZone(ZoneId.of("America/New_York")).isAfter(endEST)
            ) {
            return false;
        }
        return true;
    }
    
    /**
     * Inserts an appointment into the connected repository
     * @param appointment
     * @return the repository's unique id for the created appointment
     * @throws SQLException
     * @throws CustomerUnavailableException
     * @throws OutsideBusinessHoursException
     */
    public int create(Appointment appointment) throws SQLException, CustomerUnavailableException, OutsideBusinessHoursException, InvalidAppointmentException {
        AppointmentDto dto = mapper.toDto(appointment);
        
        LocalDate appointmentDate = appointment.getStart().toLocalDate();
        ZonedDateTime startEST = appointmentDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(Integer.valueOf(prop.getProperty("business_hours.start")));
        ZonedDateTime endEST = appointmentDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(Integer.valueOf(prop.getProperty("business_hours.end")));
        
        if (!endsAfterStart(appointment)) {
            throw new InvalidAppointmentException("Appointment end time can not occur before start time.");
        }
        
        if (!withinBusinessHours(appointment)) {
            throw new OutsideBusinessHoursException("Can not schedule appointments outside of business hours (8:00AM - 10:00PM EST).");
        }
        
        if (App.getDaoFactory().getAppointmentDao().isTimeAvailable(dto)) {
            return App.getDaoFactory().getAppointmentDao().create(dto);
        } else {
            throw new CustomerUnavailableException("Appointment can not be created because the customer in unavailable at this time.");
        }
        
    }
    
    /**
     * Queries the connected repository for appointments made by the selected user
     * based on appointment start and end times
     * @param userId
     * @param start
     * @param end
     * @return a List of Appointment objects within the selected time window
     * @throws SQLException
     */
    public List<Appointment> getByUser(int userId, LocalDateTime start, LocalDateTime end) throws SQLException {
        List<AppointmentDto> dtos = App.getDaoFactory().getAppointmentDao().getByUser(userId, start, end);
        List<Appointment> domains = new ArrayList<>();
        
        
        for (AppointmentDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    }
    
    /**
     * Queries the connect repository for appointments associated to the selected
     * contact
     * @param contactId
     * @return a List of Appointment objects with the selected contact
     * @throws SQLException
     */
    public List<Appointment> getByContact(int contactId) throws SQLException {
        List<AppointmentDto> dtos = App.getDaoFactory().getAppointmentDao().getByContact(contactId);
        List<Appointment> domains = new ArrayList<>();
        
        
        for (AppointmentDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    }
    
    /**
     * Queries the connected repository for appointments made by the selected
     * userId beginning on/after startBegin and ending on/before startEnd
     * @param userId
     * @param startBegin
     * @param startEnd
     * @return a List of upcoming Appointment objects
     * @throws SQLException
     */
    public List<Appointment> getUpcoming(int userId, LocalDateTime startBegin, LocalDateTime startEnd) throws SQLException {
        List<AppointmentDto> dtos = App.getDaoFactory().getAppointmentDao().getUpcoming(userId, startBegin, startEnd);
        List<Appointment> domains = new ArrayList<>();
        
        
        for (AppointmentDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    }
    
    /**
     * Updates the selected appointment in the connected repository
     * @param appointment
     * @return a boolean based on the success of the update
     * @throws SQLException
     * @throws OutsideBusinessHoursException
     * @throws CustomerUnavailableException
     */
    public boolean update(Appointment appointment) throws SQLException, OutsideBusinessHoursException, CustomerUnavailableException, InvalidAppointmentException {
        AppointmentDto dto = mapper.toDto(appointment);
        
        LocalDate appointmentDate = appointment.getStart().toLocalDate();
        ZonedDateTime startEST = appointmentDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(Integer.valueOf(prop.getProperty("business_hours.start")));
        ZonedDateTime endEST = appointmentDate.atStartOfDay(ZoneId.of("America/New_York")).plusHours(Integer.valueOf(prop.getProperty("business_hours.end")));
        
        if (!endsAfterStart(appointment)) {
            throw new InvalidAppointmentException("Appointment end time can not occur before start time.");
        }
        
        if (!withinBusinessHours(appointment)) {
            throw new OutsideBusinessHoursException("Can not schedule appointments outside of business hours (8:00AM - 10:00PM EST).");
        }
        
        if (App.getDaoFactory().getAppointmentDao().isTimeAvailable(dto)) {
            return App.getDaoFactory().getAppointmentDao().update(dto);
        } else {
            throw new CustomerUnavailableException("Appointment can not be created because the customer in unavailable at this time.");
        }
    }
    
    /**
     * Deletes the selected appointment from the connected repository
     * @param appointment
     * @return a boolean based on the success of the delete
     * @throws SQLException
     */
    public boolean delete(Appointment appointment) throws SQLException {
        AppointmentDto dto = mapper.toDto(appointment);
        return App.getDaoFactory().getAppointmentDao().delete(dto);
    }
    
    /**
     * Deletes all appointments associated to the selected customer
     * @param customerId
     * @return the number of deleted appointments
     * @throws SQLException
     */
    public int deleteByCustomer(int customerId) throws SQLException {
        return App.getDaoFactory().getAppointmentDao().deleteByCustomer(customerId);
    }
    
    /**
     * Tests whether the time slot for the selected appointment is free of
     * conflicting appointments
     * @param appointment
     * @return
     * @throws SQLException
     */
    public boolean isTimeAvailable(Appointment appointment) throws SQLException {
        AppointmentDto dto = mapper.toDto(appointment);
        return App.getDaoFactory().getAppointmentDao().isTimeAvailable(dto);
    }
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet aggregateByTypeMonth() throws SQLException {
        return App.getDaoFactory().getAppointmentDao().aggregateByTypeMonth();
    }
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public ResultSet aggregateByCustomer() throws SQLException {
        return App.getDaoFactory().getAppointmentDao().aggregateByCustomer();
    }
}
