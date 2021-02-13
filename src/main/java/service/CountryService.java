/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.App;
import dto.CountryDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Country;

/**
 *
 * @author dane
 */
public class CountryService {

    /**
     *
     */
    private final CountryMapper mapper = new CountryMapper();
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public List<Country> getAll() throws SQLException {
        List<CountryDto> dtos = App.getDaoFactory().getCountryDao().getAll();
        List<Country> domains = new ArrayList<>();
        
        
        for (CountryDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    };
}
