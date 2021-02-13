/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import dto.DivisionDto;
import java.sql.SQLException;
import model.Country;
import model.Division;

/**
 *
 * @author dane
 */
public class DivisionMapper implements Mapper<DivisionDto, Division> {

    @Override
    public DivisionDto toDto(Division division) {
        return new DivisionDto(
            division.getDivisionId(),
            division.getDivision(),
            division.getCreateDate(),
            division.getCreatedBy(),
            division.getLastUpdate(),
            division.getLastUpdatedBy(),
            division.getCountry().getCountryId()
        );
    }

    @Override
    public Division toDomain(DivisionDto divisionDto, DaoFactory daoFactory) throws SQLException {
        CountryMapper mapper = new CountryMapper();
        Country country = mapper.toDomain(daoFactory.getCountryDao().getById(divisionDto.getCountryId()), daoFactory);
        return new Division(
            divisionDto.getDivisionId(),
            divisionDto.getDivision(),
            divisionDto.getCreateDate(),
            divisionDto.getCreatedBy(),
            divisionDto.getLastUpdate(),
            divisionDto.getLastUpdatedBy(),
            country
        );
    }
    
}
