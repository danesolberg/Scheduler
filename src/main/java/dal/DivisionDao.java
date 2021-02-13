/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dal;

import dto.DivisionDto;
import java.util.List;

/**
 *
 * @author dane
 */
public interface DivisionDao {

    /**
     *
     * @param id
     * @return
     */
    DivisionDto getById(int id);
    
    /**
     *
     * @return
     */
    List<DivisionDto> getAll();
}
