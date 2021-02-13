/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import dto.CountryDto;
import model.Country;

/**
 *
 * @author dane
 */
public class CountryMapper implements Mapper<CountryDto, Country> {
    @Override
    public CountryDto toDto(Country country) {
        return new CountryDto(
            country.getCountryId(),
            country.getCountry(),
            country.getCreateDate(),
            country.getCreatedBy(),
            country.getLastUpdate(),
            country.getLastUpdatedBy()
        );
    }

    @Override
    public Country toDomain(CountryDto countryDto, DaoFactory daoFactory) {
        return new Country(
            countryDto.getCountryId(),
            countryDto.getCountry(),
            countryDto.getCreateDate(),
            countryDto.getCreatedBy(),
            countryDto.getLastUpdate(),
            countryDto.getLastUpdatedBy()
        );
    }
    
}
