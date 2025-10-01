package com.github.MathCunha16.integrationtests.controllers.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.github.MathCunha16.integrationtests.dto.wrappers.xml.PagedModelPerson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
class PersonControllerXMLTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;

	private static XmlMapper objectMapper;

	private static PersonDTO personDTO;

	@BeforeAll
	static void setUp() {
		objectMapper = new XmlMapper();
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

		var content = given(specification).accept(MediaType.APPLICATION_XML_VALUE)
				.contentType(MediaType.APPLICATION_XML_VALUE).body(personDTO).when().post()
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

		var content = given(specification).accept(MediaType.APPLICATION_XML_VALUE).contentType(MediaType.APPLICATION_XML_VALUE)
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

		var content = given(specification).accept(MediaType.APPLICATION_XML_VALUE).contentType(MediaType.APPLICATION_XML_VALUE).body(personDTO).when().put()
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

		var content = given(specification).accept(MediaType.APPLICATION_XML_VALUE).contentType(MediaType.APPLICATION_XML_VALUE)
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

		given(specification).accept(MediaType.APPLICATION_XML_VALUE).contentType(MediaType.APPLICATION_XML_VALUE).pathParam("id", personDTO.getId()).when()
				.delete("{id}").then().statusCode(204);

	}

	@Test
	@Order(6)
    void testFindAll() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_XML_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_XML_VALUE)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        List<PersonDTO> people = wrapper.getContent();


        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Alfredo", personOne.getFirstName());
        assertEquals("McInnes", personOne.getLastName());
        assertEquals("5 Fremont Place", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertFalse(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Alissa", personFour.getFirstName());
        assertEquals("Kall", personFour.getLastName());
        assertEquals("55778 Crest Line Center", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertFalse(personFour.getEnabled());
    }

	private void mockPerson() {
		personDTO.setFirstName("Matheus");
		personDTO.setLastName("Cunha");
		personDTO.setAddress("Rua 1");
		personDTO.setGender("Male");
		personDTO.setEnabled(true);
	}
}
