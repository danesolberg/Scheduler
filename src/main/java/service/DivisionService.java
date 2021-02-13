/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import app.App;
import dto.DivisionDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Division;

/**
 *
 * @author dane
 */
public class DivisionService {

    /**
     *
     */
    private final DivisionMapper mapper = new DivisionMapper();
    
    /**
     *
     * @return
     * @throws SQLException
     */
    public List<Division> getAll() throws SQLException {
        List<DivisionDto> dtos = App.getDaoFactory().getDivisionDao().getAll();
        List<Division> domains = new ArrayList<>();
        
        
        for (DivisionDto dto : dtos) {
            domains.add(mapper.toDomain(dto, App.getDaoFactory()));
        }
        
        return domains;
    }
}
