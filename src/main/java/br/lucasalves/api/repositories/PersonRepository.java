package br.lucasalves.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.lucasalves.api.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {}
