package com.github.MathCunha16.integrationtests.controllers.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.MathCunha16.config.TestConfigs;
import com.github.MathCunha16.data.dto.BookDTO;
import com.github.MathCunha16.integrationtests.dto.wrappers.json.WrapperBookDTO;
import com.github.MathCunha16.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static BookDTO book;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Add support for Java 8 date/time
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookDTO();
        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockBook();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(book)) // Serialize to ensure proper date format
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
        book = createdBook;

        assertNotNull(createdBook.getId(), "Book ID should not be null");
        assertTrue(createdBook.getId() > 0, "Book ID should be positive");
        assertEquals("Docker Deep Dive", createdBook.getTitle());
        assertEquals("Nigel Poulton", createdBook.getAuthor());
        assertEquals(BigDecimal.valueOf(55.99), createdBook.getPrice());
        assertEquals(LocalDateTime.of(2025, 9, 30, 14, 30), createdBook.getLaunchDate());
    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        assertNotNull(book.getId(), "Book ID must be set before update"); // Ensure ID exists
        book.setTitle("Docker Deep Dive - Updated");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(book)) // Serialize to ensure proper date format
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO updatedBook = objectMapper.readValue(content, BookDTO.class);
        book = updatedBook;

        assertNotNull(updatedBook.getId(), "Updated book ID should not be null");
        assertTrue(updatedBook.getId() > 0, "Updated book ID should be positive");
        assertEquals("Docker Deep Dive - Updated", updatedBook.getTitle());
        assertEquals("Nigel Poulton", updatedBook.getAuthor());
        assertEquals(BigDecimal.valueOf(55.99), updatedBook.getPrice());
        assertEquals(LocalDateTime.of(2025, 9, 30, 14, 30), updatedBook.getLaunchDate());
    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {
        assertNotNull(book.getId(), "Book ID must be set before findById"); // Ensure ID exists

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        BookDTO foundBook = objectMapper.readValue(content, BookDTO.class);

        assertNotNull(foundBook.getId(), "Found book ID should not be null");
        assertTrue(foundBook.getId() > 0, "Found book ID should be positive");
        assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        assertEquals("Nigel Poulton", foundBook.getAuthor());
        assertEquals(BigDecimal.valueOf(55.99), foundBook.getPrice());
        assertEquals(LocalDateTime.of(2025, 9, 30, 14, 30), foundBook.getLaunchDate());
    }

    @Test
    @Order(4)
    void deleteTest() {
        assertNotNull(book.getId(), "Book ID must be set before delete"); // Ensure ID exists

        given(specification)
                .pathParam("id", book.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    @Disabled
    void findAllTest() throws JsonProcessingException {
        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperBookDTO wrapper = objectMapper.readValue(content, WrapperBookDTO.class);
        var books = wrapper.getEmbedded().getBooks();

        assertFalse(books.isEmpty(), "Book list should not be empty");

        BookDTO bookOne = books.get(0);
        assertNotNull(bookOne.getId(), "First book ID should not be null");
        assertNotNull(bookOne.getTitle(), "First book title should not be null");
        assertNotNull(bookOne.getAuthor(), "First book author should not be null");
        assertNotNull(bookOne.getPrice(), "First book price should not be null");
        assertTrue(bookOne.getId() > 0, "First book ID should be positive");
        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", bookOne.getTitle());
        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", bookOne.getAuthor());
        assertEquals(BigDecimal.valueOf(54.00), bookOne.getPrice());

        BookDTO bookFive = books.stream()
                .filter(b -> b.getTitle().equals("Domain Driven Design"))
                .findFirst()
                .orElse(null);
        assertNotNull(bookFive, "Domain Driven Design book should exist");
        assertNotNull(bookFive.getId(), "Book ID should not be null");
        assertNotNull(bookFive.getTitle(), "Book title should not be null");
        assertNotNull(bookFive.getAuthor(), "Book author should not be null");
        assertNotNull(bookFive.getPrice(), "Book price should not be null");
        assertTrue(bookFive.getId() > 0, "Book ID should be positive");
        assertEquals("Domain Driven Design", bookFive.getTitle());
        assertEquals("Eric Evans", bookFive.getAuthor());
        assertEquals(BigDecimal.valueOf(92.00), bookFive.getPrice());
    }

    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(BigDecimal.valueOf(55.99));
        book.setLaunchDate(LocalDateTime.of(2025, 9, 30, 14, 30));
    }
}