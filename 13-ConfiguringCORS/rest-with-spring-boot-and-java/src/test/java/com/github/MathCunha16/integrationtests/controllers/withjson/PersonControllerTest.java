package com.github.MathCunha16.integrationtests.controllers.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
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
class PersonControllerTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	
	private static ObjectMapper objectMapper;
	
	private static PersonDTO personDTO;
	
	@BeforeAll
	static void setUp(){
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		personDTO = new PersonDTO();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonProcessingException{
		mockPerson();
		specification = new RequestSpecBuilder()
	            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
	            	.setBasePath("/api/person/v1")
	            		.setPort(TestConfigs.SERVER_PORT)
	            			.addFilter(new RequestLoggingFilter(LogDetail.ALL))
	            			.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	            				.build();
		
		
		var content = given(specification)
	            .contentType(MediaType.APPLICATION_JSON_VALUE)
	                .body(personDTO)
	                	.when()
	                		.post()
	                			.then()
	                				.statusCode(200)
	                					.extract()
	                						.body()
	                							.asString();
		
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Matheus", createdPerson.getFirstName());
        assertEquals("Cunha", createdPerson.getLastName());
        assertEquals("Rua 1", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
	}
	
	@Test
    @Order(2)
    void createWithWrongOrigin() throws JsonProcessingException {

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personDTO)
            .when()
                .post()
            .then()
                .statusCode(403)
            .extract()
                .body()
                    .asString();

        assertEquals("Invalid CORS request", content);

    }
	

    @Test
    @Order(3)
    void findById() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        personDTO = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Matheus", createdPerson.getFirstName());
        assertEquals("Cunha", createdPerson.getLastName());
        assertEquals("Rua 1", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }
    @Test
    @Order(4)
    void findByIdWithWrongOrigin() throws JsonProcessingException {
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", personDTO.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();

        assertEquals("Invalid CORS request", content);
    }

	
	@Test
	void testUpdate() {
		fail("Not yett implemented");
	}

	@Test
	void testFindAll() {
		fail("Not yet implemented");
	}
	
	private void mockPerson() {
		personDTO.setFirstName("Matheus");
		personDTO.setLastName("Cunha");
		personDTO.setAddress("Rua 1");
		personDTO.setGender("Male");
	}
}
