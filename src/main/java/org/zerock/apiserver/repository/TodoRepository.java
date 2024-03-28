package org.zerock.apiserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.apiserver.domain.Todo;
import org.zerock.apiserver.repository.search.TodoSearch;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoSearch {
}
