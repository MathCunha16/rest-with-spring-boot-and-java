package com.github.MathCunha16.services;

import com.github.MathCunha16.controllers.BookController;
import com.github.MathCunha16.data.dto.BookDTO;
import com.github.MathCunha16.exceptions.RequiredObjectIsNullException;
import com.github.MathCunha16.exceptions.ResourceNotFoundException;
import com.github.MathCunha16.model.Book;
import com.github.MathCunha16.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static com.github.MathCunha16.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	PagedResourcesAssembler<BookDTO> assembler;

	public PagedModel<EntityModel<BookDTO>> findAll(Pageable pageable) {

		var books = bookRepository.findAll(pageable);

		var booksWithLinks = books.map(book ->{
			BookDTO dto = parseObject(book, BookDTO.class);
			addHateoasLinks(dto);
			return dto;
		});

		Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).
				findAll(pageable.getPageNumber(),
						pageable.getPageSize(),
						pageable.getSort().toString())).withSelfRel();

		return assembler.toModel(booksWithLinks, findAllLink);
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

		dto.add(linkTo(methodOn(BookController.class).findAll(1,2,"asc")).withRel("findAll").withType("GET"));

		dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));

		dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));

		dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
	}

}
