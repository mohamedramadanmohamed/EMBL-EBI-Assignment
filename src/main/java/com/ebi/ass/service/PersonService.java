package com.ebi.ass.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ebi.ass.dao.PersonRepository;
import com.ebi.ass.model.Person;

/**
 * @author MohamedRamadan
 *
 */
@Service
public class PersonService {
	@Autowired
	private PersonRepository repository;
	
	public List<Person> getAllPersons(){
		return repository.findAll();
	}
	
	public Person getPersonById(int id) {
		Optional<Person> person= repository.findById(id);
		return person.isPresent()?person.get():null;
	}
	
	public List<Person> savePerson(List<Person> persons) {
		 return repository.saveAll(persons);
	}
	
	public Person updatePerson(Person person) {
		 return repository.save(person);
	}
	
	public boolean deletePerson(int id) {
		repository.deleteById(id);
		return true;
	}
}
