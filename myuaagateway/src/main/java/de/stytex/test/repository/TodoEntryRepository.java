package de.stytex.test.repository;

import de.stytex.test.domain.TodoEntry;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TodoEntry entity.
 */
@SuppressWarnings("unused")
public interface TodoEntryRepository extends JpaRepository<TodoEntry,Long> {

}
