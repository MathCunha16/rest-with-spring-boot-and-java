package com.github.MathCunha16.integrationtests.controllers.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.github.MathCunha16.config.TestConfigs;
import com.github.MathCunha16.integrationtests.dto.PersonDTO;
import com.github.MathCunha16.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;
	private static PersonDTO personDTO;

	@BeforeAll
	static void setUp() {
		objectMapper = new YAMLMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		personDTO = new PersonDTO();

		specification = new RequestSpecBuilder().setBasePath("/api/person/v1").setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build()
				.config(RestAssured.config()
						.encoderConfig(
								EncoderConfig.encoderConfig().encodeContentTypeAs("application/yaml", ContentType.TEXT))
						.objectMapperConfig(new ObjectMapperConfig(
								new com.github.MathCunha16.integrationtests.controllers.withyaml.mapper.YAMLMapper())));
	}

	@Test
	@Order(1)
	void testCreate() {
		mockPerson();

		PersonDTO createdPerson = given().spec(specification).accept(MediaType.APPLICATION_YAML_VALUE)
				.contentType(MediaType.APPLICATION_YAML_VALUE).body(personDTO).when().post().then().statusCode(200)
				.extract().body().as(PersonDTO.class);

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
	void testFindById() {
		PersonDTO createdPerson = given().spec(specification).accept(MediaType.APPLICATION_YAML_VALUE)
				.contentType(MediaType.APPLICATION_YAML_VALUE).pathParam("id", personDTO.getId()).when().get("{id}")
				.then().statusCode(200).extract().body().as(PersonDTO.class);

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
	void testUpdate() {
		personDTO.setFirstName("Jorginho");

		PersonDTO createdPerson = given().spec(specification).accept(MediaType.APPLICATION_YAML_VALUE)
				.contentType(MediaType.APPLICATION_YAML_VALUE).body(personDTO).when().put().then().statusCode(200)
				.extract().body().as(PersonDTO.class);

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
	void testDisablePerson() {
		PersonDTO createdPerson = given().spec(specification).accept(MediaType.APPLICATION_YAML_VALUE)
				.contentType(MediaType.APPLICATION_YAML_VALUE).pathParam("id", personDTO.getId()).when().patch("{id}")
				.then().statusCode(200).extract().body().as(PersonDTO.class);

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
	void testDelete() {
		given().spec(specification).contentType(MediaType.APPLICATION_YAML_VALUE).pathParam("id", personDTO.getId())
				.when().delete("{id}").then().statusCode(204);
	}

	@Test
	@Order(6)
    @Disabled("testando")
	void testFindAll() throws JsonMappingException, JsonProcessingException {
		var content = given().spec(specification).accept(MediaType.APPLICATION_YAML_VALUE)
				.contentType(MediaType.APPLICATION_YAML_VALUE).when().get().then().statusCode(200).extract().body()
				.asString();

		List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {
		});
		PersonDTO personOne = people.get(0);

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