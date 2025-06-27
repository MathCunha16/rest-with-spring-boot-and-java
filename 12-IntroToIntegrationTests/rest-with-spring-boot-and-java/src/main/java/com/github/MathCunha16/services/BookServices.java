package com.github.MathCunha16.services;

import static com.github.MathCunha16.mapper.ObjectMapper.parseListObjects;
import static com.github.MathCunha16.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.MathCunha16.controllers.BookController;
import com.github.MathCunha16.data.dto.BookDTO;
import com.github.MathCunha16.exceptions.RequiredObjectIsNullException;
import com.github.MathCunha16.exceptions.ResourceNotFoundException;
import com.github.MathCunha16.model.Book;
import com.github.MathCunha16.repository.BookRepository;

@Service
public class BookServices {

	@Autowired
	BookRepository bookRepository;

	public List<BookDTO> findAll() {
		var books = parseListObjects(bookRepository.findAll(), BookDTO.class);
		books.forEach(this::addHateoasLinks);
		return books;
	}

	public BookDTO findById(Integer id) {

		var entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		var dto = parseObject(entity, BookDTO.class);

		addHateoasLinks(dto);

		return dto;
	}
	
	public BookDTO create(BookDTO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		var entity = parseObject(book,Book.class);
		
		var dto = parseObject(bookRepository.save(entity), BookDTO.class);
		
		addHateoasLinks(dto);
		
		return dto;
	}
	
	public BookDTO update(BookDTO book) {
		
		if(book == null) throw new RequiredObjectIsNullException();
		
		var entity = parseObject(book,Book.class);
		
		var dto = parseObject(bookRepository.save(entity), BookDTO.class);
		
		addHateoasLinks(dto);
		
		return dto;
	}
	
	public void delete(Integer id) {
		Book entity = bookRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		bookRepository.delete(entity);
	}

	private void addHateoasLinks(BookDTO dto) {
		dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));

		dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));

		dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));

		dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));

		dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
	}

}
