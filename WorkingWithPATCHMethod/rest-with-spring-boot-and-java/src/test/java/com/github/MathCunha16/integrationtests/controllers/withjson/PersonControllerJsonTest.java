package com.github.MathCunha16.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.MathCunha16.config.TestConfigs;
import com.github.MathCunha16.integrationtests.dto.PersonDTO;
import com.github.MathCunha16.integrationtests.testcontainers.AbstractIntegrationTest;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;

	private static ObjectMapper objectMapper;

	private static PersonDTO personDTO;

	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		personDTO = new PersonDTO();

		specification = new RequestSpecBuilder().setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonProcessingException {

		mockPerson();

		var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE).body(personDTO).when().post()
				.then().statusCode(200).extract().body().asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;

		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId() > 0);

		assertEquals("Matheus", createdPerson.getFirstName());
		assertEquals("Cunha", createdPerson.getLastName());
		assertEquals("Rua 1", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}

	@Test
	@Order(2)
	void testFindById() throws JsonProcessingException {

		var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", personDTO.getId()).when().get("{id}").then().statusCode(200).extract().body()
				.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;

		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId() > 0);

		assertEquals("Matheus", createdPerson.getFirstName());
		assertEquals("Cunha", createdPerson.getLastName());
		assertEquals("Rua 1", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}

	@Test
	@Order(3)
	void testUpdate() throws JsonProcessingException {

		personDTO.setFirstName("Jorginho");

		var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE).body(personDTO).when().put()
				.then().statusCode(200).extract().body().asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;

		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId() > 0);

		assertEquals("Jorginho", createdPerson.getFirstName());
		assertEquals("Cunha", createdPerson.getLastName());
		assertEquals("Rua 1", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertTrue(createdPerson.getEnabled());
	}

	@Test
	@Order(4)
	void testDisablePerson() throws JsonProcessingException {

		var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", personDTO.getId()).when().patch("{id}").then().statusCode(200).extract().body()
				.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		personDTO = createdPerson;

		assertNotNull(createdPerson.getId());
		assertTrue(createdPerson.getId() > 0);

		assertEquals("Jorginho", createdPerson.getFirstName());
		assertEquals("Cunha", createdPerson.getLastName());
		assertEquals("Rua 1", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertFalse(createdPerson.getEnabled());
	}

	@Test
	@Order(5)
	void testDelete() throws JsonProcessingException {

		given(specification).contentType(MediaType.APPLICATION_JSON_VALUE).pathParam("id", personDTO.getId()).when()
				.delete("{id}").then().statusCode(204);

	}

	@Test
	@Order(6)
	void testFindAll() throws JsonMappingException, JsonProcessingException, InterruptedException {
		
			var content = given(specification).accept(MediaType.APPLICATION_JSON_VALUE).when().get()
					.then().statusCode(200).extract().body().asString();
			
			List <PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});

			PersonDTO personOne = people.get(0);
			personDTO = personOne;

			assertNotNull(personOne.getId());
			assertTrue(personOne.getId() > 0);

			assertEquals("Matheus", personOne.getFirstName());
			assertEquals("Prado", personOne.getLastName());
			assertEquals("Goiânia - Brasil", personOne.getAddress());
			assertEquals("male", personOne.getGender());
			assertTrue(personOne.getEnabled());
			
	}

	private void mockPerson() {
		personDTO.setFirstName("Matheus");
		personDTO.setLastName("Cunha");
		personDTO.setAddress("Rua 1");
		personDTO.setGender("Male");
		personDTO.setEnabled(true);
	}
}
