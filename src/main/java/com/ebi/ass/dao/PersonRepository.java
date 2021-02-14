package com.ebi.ass.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ebi.ass.model.Person;

/**
 * @author MohamedRamadan
 *
 */
public interface PersonRepository extends JpaRepository<Person, Integer>{
	
}
