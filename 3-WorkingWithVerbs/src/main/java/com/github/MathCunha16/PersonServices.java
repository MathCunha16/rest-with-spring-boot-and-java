package com.github.MathCunha16;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.github.MathCunha16.model.Person;

@Service
public class PersonServices {

	private final AtomicLong counter = new AtomicLong();
	private Logger logger = Logger.getLogger(PersonServices.class.getName());

	public Person findById(String id) {
		logger.info("Finding one person!");

		Person person = new Person();

		person.setId(counter.incrementAndGet());
		person.setFisrtName("Matheus");
		person.setLastName("Cunha");
		person.setAddress("Goiânia - Goiás - Brasil");
		person.setGender("Male");

		return person;
	}

	public List<Person> findAll() {

		logger.info("Finding all people!");

		List<Person> persons = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}
		return persons;
	}

	public Person create(Person person) {

		logger.info("Creating one person!");

		return person;
	}

	public Person update(Person person) {

		logger.info("Updating one person!");

		return person;
	}

	public void delete(String id) {

		logger.info("Deleting one person!");
	}

	private Person mockPerson(int i) {
		Person person = new Person();

		person.setId(counter.incrementAndGet());
		person.setFisrtName("First Name " + i);
		person.setLastName("Last Name" + i);
		person.setAddress("Some address in Brazil");
		person.setGender("Male");

		return person;
	}
}
