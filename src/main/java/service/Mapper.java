/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dal.DaoFactory;
import java.sql.SQLException;

/**
 *
 * @author dane
 * @param <Dto>
 * @param <Domain>
 */
public interface Mapper<Dto, Domain> {

    /**
     *
     * @param domain
     * @return
     */
    Dto toDto(Domain domain);

    /**
     *
     * @param dto
     * @param daoFactory
     * @return
     * @throws SQLException
     */
    Domain toDomain(Dto dto, DaoFactory daoFactory) throws SQLException;
}
