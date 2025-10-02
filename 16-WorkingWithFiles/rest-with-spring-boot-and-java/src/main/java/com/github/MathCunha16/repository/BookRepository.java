package com.github.MathCunha16.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.MathCunha16.model.Book;

public interface BookRepository extends JpaRepository <Book, Integer> {

}
