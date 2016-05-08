package de.stytex.test.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.stytex.test.domain.TodoEntry;
import de.stytex.test.repository.TodoEntryRepository;
import de.stytex.test.web.rest.util.HeaderUtil;
import de.stytex.test.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing TodoEntry.
 */
@RestController
@RequestMapping("/api")
public class TodoEntryResource {

    private final Logger log = LoggerFactory.getLogger(TodoEntryResource.class);
        
    @Inject
    private TodoEntryRepository todoEntryRepository;
    
    /**
     * POST  /todo-entries : Create a new todoEntry.
     *
     * @param todoEntry the todoEntry to create
     * @return the ResponseEntity with status 201 (Created) and with body the new todoEntry, or with status 400 (Bad Request) if the todoEntry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/todo-entries",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TodoEntry> createTodoEntry(@Valid @RequestBody TodoEntry todoEntry) throws URISyntaxException {
        log.debug("REST request to save TodoEntry : {}", todoEntry);
        if (todoEntry.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("todoEntry", "idexists", "A new todoEntry cannot already have an ID")).body(null);
        }
        TodoEntry result = todoEntryRepository.save(todoEntry);
        return ResponseEntity.created(new URI("/api/todo-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("todoEntry", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /todo-entries : Updates an existing todoEntry.
     *
     * @param todoEntry the todoEntry to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated todoEntry,
     * or with status 400 (Bad Request) if the todoEntry is not valid,
     * or with status 500 (Internal Server Error) if the todoEntry couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/todo-entries",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TodoEntry> updateTodoEntry(@Valid @RequestBody TodoEntry todoEntry) throws URISyntaxException {
        log.debug("REST request to update TodoEntry : {}", todoEntry);
        if (todoEntry.getId() == null) {
            return createTodoEntry(todoEntry);
        }
        TodoEntry result = todoEntryRepository.save(todoEntry);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("todoEntry", todoEntry.getId().toString()))
            .body(result);
    }

    /**
     * GET  /todo-entries : get all the todoEntries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of todoEntries in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/todo-entries",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TodoEntry>> getAllTodoEntries(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TodoEntries");
        Page<TodoEntry> page = todoEntryRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/todo-entries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /todo-entries/:id : get the "id" todoEntry.
     *
     * @param id the id of the todoEntry to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the todoEntry, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/todo-entries/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TodoEntry> getTodoEntry(@PathVariable Long id) {
        log.debug("REST request to get TodoEntry : {}", id);
        TodoEntry todoEntry = todoEntryRepository.findOne(id);
        return Optional.ofNullable(todoEntry)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /todo-entries/:id : delete the "id" todoEntry.
     *
     * @param id the id of the todoEntry to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/todo-entries/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTodoEntry(@PathVariable Long id) {
        log.debug("REST request to delete TodoEntry : {}", id);
        todoEntryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("todoEntry", id.toString())).build();
    }

}
