package com.ebi.ass.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ebi.ass.exception.PersonNotFoundException;
import com.ebi.ass.model.Person;
import com.ebi.ass.service.PersonService;

/**
 * @author MohamedRamadan
 *
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin
public class PersonController {

	@Autowired
	private PersonService service;
	
	//get all persons
	@GetMapping("/persons") 
	public ResponseEntity<Map<String, List<Person>>> getAllPersons(){
		List<Person> persons= service.getAllPersons();
		Map<String, List<Person>> res= new HashMap<String, List<Person>>();
		res.put("person", persons);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
	//get specific person by id
	@GetMapping("/persons/{id}")
	public ResponseEntity<Map<String, Person>> getPerson(@PathVariable(value="id")int  id){
		Person person= service.getPersonById(id);
		
		if(person==null) 
			throw new PersonNotFoundException("Person not found for this id :: " + id);
		Map<String, Person> res= new HashMap<>();
		res.put("person", person);
		return ResponseEntity.status(HttpStatus.OK).body(res);
	}
	
	// create new person
	@PostMapping("/persons")
	public ResponseEntity<Map<String, Object>> createPerson(@RequestBody Map<String, List<Person>> json) {
		List<Person> persons= service.savePerson(json.get("person"));
		List<URI> uris= new ArrayList<URI>();
		Map<String, Object> res= new HashMap<>();
		res.put("Status", "Persons successfully created with uris below...");
		for (Person person : persons) {
			URI uri= ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(person.getId()).toUri();
			uris.add(uri);
		}
		res.put("URIS", uris);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
	
	//update specific person by id
	@PutMapping("/persons/{id}")
	public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") int id, 
			@RequestBody Person curPerson){
		Person person= service.getPersonById(id);
		if(person==null) throw new PersonNotFoundException("Person not found for this id :: " + id);
		person.setFirstName(curPerson.getFirstName());
		person.setLastName(curPerson.getLastName());
		person.setAge(curPerson.getAge());
		person.setFavouriteColour(curPerson.getFavouriteColour());
		return ResponseEntity.status(HttpStatus.OK).body(service.updatePerson(person));
	}
	
	//delete specific person by id
	@DeleteMapping("/persons/{id}")
	public ResponseEntity<Map<String, Boolean>> deletePerson(@PathVariable(value = "id") int id){
		Person person= service.getPersonById(id);
		if(person==null) throw new PersonNotFoundException("Person not found for this id :: " + id);
		service.deletePerson(id);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
}
