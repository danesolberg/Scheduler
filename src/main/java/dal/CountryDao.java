/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.CountryDto;
import java.util.List;

/**
 *
 * @author dane
 */
public interface CountryDao {

    /**
     * Queries the repository for a country by id
     * @param id
     * @return return the matching country if one exists
     */
    CountryDto getById(int id);
    
    /**
     * Queries the repository for all countries
     * @return a list of all countries
     */
    List<CountryDto> getAll();
}
