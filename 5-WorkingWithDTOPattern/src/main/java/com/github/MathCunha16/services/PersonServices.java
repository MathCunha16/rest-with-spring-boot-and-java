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

import com.github.MathCunha16.exceptions.ResourceNotFoundException;
import com.github.MathCunha16.model.Person;
import com.github.MathCunha16.repository.PersonRepository;

@Service
public class PersonServices {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;


    public List<Person> findAll() {

        logger.info("Finding all People!");

        return parseListObjects(repository.findAll(), Person.class);
    }

    public Person findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return parseObject(entity, Person.class);
    }

    public Person create(Person person) {

        logger.info("Creating one Person!");
        var entity = parseObject(person, Person.class);

        return parseObject(repository.save(entity), Person.class);
    }

    public Person update(Person person) {

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(repository.save(entity), Person.class);
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}
