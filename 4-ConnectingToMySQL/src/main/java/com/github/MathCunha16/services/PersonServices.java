package com.github.MathCunha16.services;

import java.util.List;
// import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.MathCunha16.exceptions.ResourceNotFoundException;
import com.github.MathCunha16.model.Person;
import com.github.MathCunha16.repository.PersonRepository;

@Service
public class PersonServices {

	// private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;

	public Person findById(Long id) {
		logger.info("Finding one person!");

		return repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
	}

	public List<Person> findAll() {

		logger.info("Finding all people!");

		return repository.findAll();
	}

	public Person create(Person person) {

		logger.info("Creating one person!");

		return repository.save(person);
	}

	public Person update(Person person) {

		logger.info("Updating one person!");

		Person entity = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		entity.setFisrtName(person.getFisrtName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		return repository.save(person);
	}

	public void delete(Long id) {

		logger.info("Deleting one person!");

		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		repository.delete(entity);
	}

//	private Person mockPerson(int i) {
//		Person person = new Person();
//
//		person.setId(counter.incrementAndGet());
//		person.setFisrtName("First Name " + i);
//		person.setLastName("Last Name" + i);
//		person.setAddress("Some address in Brazil");
//		person.setGender("Male");
//
//		return person;
//	}

}
