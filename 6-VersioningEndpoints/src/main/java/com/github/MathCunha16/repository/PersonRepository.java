package com.github.MathCunha16.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.MathCunha16.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {}
