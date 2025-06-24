package com.github.MathCunha16.unitetests.mapper.mocks;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.github.MathCunha16.data.dto.BookDTO;
import com.github.MathCunha16.model.Book;

public class MockBook {


    public Book mockEntity() {
        return mockEntity(0);
    }
    
    public BookDTO mockDTO() {
        return mockDTO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookDTO> mockDTOList() {
        List<BookDTO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockDTO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.intValue());
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(LocalDateTime.now());
        book.setPrice(new BigDecimal("25.0"));
        book.setTitle("Some Title" + number);
        return book;
    }

    public BookDTO mockDTO(Integer number) {
        BookDTO book = new BookDTO();
        book.setId(number.intValue());
        book.setAuthor("Some Author" + number);
        book.setLaunchDate(LocalDateTime.now());
        book.setPrice(new BigDecimal("25.0"));
        book.setTitle("Some Title" + number);
        return book;
    }

}
