package com.github.MathCunha16.unittests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.MathCunha16.data.dto.BookDTO;
import com.github.MathCunha16.exceptions.RequiredObjectIsNullException;
import com.github.MathCunha16.model.Book;
import com.github.MathCunha16.repository.BookRepository;
import com.github.MathCunha16.services.BookServices;
import com.github.MathCunha16.unittests.mapper.mocks.MockBook;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

	MockBook input;

	@InjectMocks
	private BookServices bookServices;

	@Mock
	BookRepository bookRepository;

	@BeforeEach
	void setUp() {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testFindById() {
		Book entity = input.mockEntity(1);

		when(bookRepository.findById(1)).thenReturn(Optional.of(entity));

		var result = bookServices.findById(1);

		// Assert (Verificar os resultados)
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		// Verificando os links HATEOAS
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().equals("</api/book/v1/1>;rel=\"self\";type=\"GET\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().equals("</api/book/v1>;rel=\"findAll\";type=\"GET\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"create\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"update\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"delete\"")));

		// Verificando o conteúdo do DTO retornado
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(new BigDecimal("25.0"), result.getPrice());
		assertEquals("Some Title1", result.getTitle());
		assertNotNull(result.getLaunchDate());
	}

	@Test
	void testCreate() {
		Book persistedEntity = input.mockEntity(1);

		BookDTO inputDTO = input.mockDTO(1);
		inputDTO.setId(null); // Simulando um objeto novo que ainda não foi salvo

		when(bookRepository.save(any(Book.class))).thenReturn(persistedEntity);

		var result = bookServices.create(inputDTO);

		// --- Fase 3: Assert (Verificar os resultados) ---
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());

		// Verifica se o ID foi atribuído corretamente pelo mock
		assertEquals(1, result.getId());

		// Verificação do conteúdo
		assertEquals(1, result.getId());
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(new BigDecimal("25.0"), result.getPrice());
		assertEquals("Some Title1", result.getTitle());
		assertNotNull(result.getLaunchDate());

		// Verificando os links HATEOAS
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().equals("</api/book/v1/1>;rel=\"self\";type=\"GET\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().equals("</api/book/v1>;rel=\"findAll\";type=\"GET\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"create\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"update\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"delete\"")));
	}
	
	@Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
        () -> {
            bookServices.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

	@Test
	void testUpdate() {
		Book entity = input.mockEntity(1);
		Book persistedEntity = entity;
		
		BookDTO dto = input.mockDTO(1);
		
		// Mock para o save, que é o único método do repositório chamado pelo serviço 'update'
		when(bookRepository.save(any(Book.class))).thenReturn(persistedEntity);
		
		var result = bookServices.update(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getLinks());
		
		assertEquals(1, result.getId());
		assertEquals("Some Author1", result.getAuthor());
		assertEquals(new BigDecimal("25.0"), result.getPrice());
		assertEquals("Some Title1", result.getTitle());
		assertNotNull(result.getLaunchDate());
		
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"self\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"findAll\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"create\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"update\"")));
		assertTrue(result.getLinks().stream().anyMatch(link -> link.toString().contains("rel=\"delete\"")));
	}
	
	@Test
	void testUpdateWithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class,
		() -> {
			bookServices.update(null);
		});

		String expectedMessage = "It is not allowed to persist a null object!";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testDelete() {
		Book entity = input.mockEntity(1);
		
		when(bookRepository.findById(1)).thenReturn(Optional.of(entity));
		
		bookServices.delete(1);
		
		verify(bookRepository, times(1)).delete(entity);
	}

//	@Test
//	@Disabled("Still under development XD")
//	void testFindAll() {
//		List<Book> entityList = input.mockEntityList();
//
//		when(bookRepository.findAll()).thenReturn(entityList);
//
//		var resultList = bookServices.findAll();
//
//		assertNotNull(resultList);
//		assertEquals(14, resultList.size());
//
//		// Verifica o primeiro item da lista
//		var firstBook = resultList.get(0);
//
//		assertNotNull(firstBook);
//		assertNotNull(firstBook.getId());
//		assertNotNull(firstBook.getLinks());
//
//		assertEquals(0, firstBook.getId());
//		assertEquals("Some Author0", firstBook.getAuthor());
//		assertEquals(new BigDecimal("25.0"), firstBook.getPrice());
//		assertEquals("Some Title0", firstBook.getTitle());
//		assertNotNull(firstBook.getLaunchDate());
//
//		assertTrue(firstBook.getLinks().stream().anyMatch(link -> link.toString().equals("</api/book/v1/0>;rel=\"self\";type=\"GET\"")));
//
//		// Verifica o sétimo item da lista
//		var seventhBook = resultList.get(7);
//
//		assertNotNull(seventhBook);
//		assertNotNull(seventhBook.getId());
//		assertNotNull(seventhBook.getLinks());
//
//		assertEquals(7, seventhBook.getId());
//		assertEquals("Some Author7", seventhBook.getAuthor());
//		assertEquals(new BigDecimal("25.0"), seventhBook.getPrice());
//		assertEquals("Some Title7", seventhBook.getTitle());
//		assertNotNull(seventhBook.getLaunchDate());
//
//		assertTrue(seventhBook.getLinks().stream().anyMatch(link -> link.toString().equals("</api/book/v1/7>;rel=\"self\";type=\"GET\"")));
//	}
}
