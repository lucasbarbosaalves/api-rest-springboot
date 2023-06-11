package br.lucasalves.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.lucasalves.api.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {}
