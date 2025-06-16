package com.github.MathCunha16.services;

import static com.github.MathCunha16.mapper.ObjectMapper.parseListObjects;
import static com.github.MathCunha16.mapper.ObjectMapper.parseObject;

import java.util.List;
// import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.MathCunha16.data.dto.v1.PersonDTO;
import com.github.MathCunha16.data.dto.v2.PersonDTOv2;
import com.github.MathCunha16.exceptions.ResourceNotFoundException;
import com.github.MathCunha16.mapper.custom.PersonMapper;
import com.github.MathCunha16.model.Person;
import com.github.MathCunha16.repository.PersonRepository;

@Service
public class PersonServices {

	private final AtomicLong counter = new AtomicLong();
	private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

	@Autowired
	PersonRepository repository;
	@Autowired
	PersonMapper converter;

	public List<PersonDTO> findAll() {

		logger.info("Finding all People!");

		return parseListObjects(repository.findAll(), PersonDTO.class);
	}

	public PersonDTO findById(Long id) {
		logger.info("Finding one Person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return parseObject(entity, PersonDTO.class);
	}

	public PersonDTO create(PersonDTO person) {

		logger.info("Creating one Person!");
		var entity = parseObject(person, Person.class);

		return parseObject(repository.save(entity), PersonDTO.class);
	}

	public PersonDTOv2 createV2(PersonDTOv2 person) {

		logger.info("Creating one Person V2!");
		var entity = converter.convertDTOtoEntity(person);

		return converter.convertEntityToDTO(repository.save(entity));
	}

	public PersonDTO update(PersonDTO person) {

		logger.info("Updating one Person!");
		Person entity = repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());

		return parseObject(repository.save(entity), PersonDTO.class);
	}

	public void delete(Long id) {

		logger.info("Deleting one Person!");

		Person entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
	}
}
