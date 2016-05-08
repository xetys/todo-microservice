package de.stytex.test.web.rest;

import de.stytex.test.MyuaagatewayApp;
import de.stytex.test.domain.TodoEntry;
import de.stytex.test.repository.TodoEntryRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TodoEntryResource REST controller.
 *
 * @see TodoEntryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyuaagatewayApp.class)
@WebAppConfiguration
@IntegrationTest
public class TodoEntryResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";

    private static final Integer DEFAULT_PERCENT_DONE = 0;
    private static final Integer UPDATED_PERCENT_DONE = 1;

    private static final LocalDate DEFAULT_DEAD_LINE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DEAD_LINE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private TodoEntryRepository todoEntryRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTodoEntryMockMvc;

    private TodoEntry todoEntry;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TodoEntryResource todoEntryResource = new TodoEntryResource();
        ReflectionTestUtils.setField(todoEntryResource, "todoEntryRepository", todoEntryRepository);
        this.restTodoEntryMockMvc = MockMvcBuilders.standaloneSetup(todoEntryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        todoEntry = new TodoEntry();
        todoEntry.setTitle(DEFAULT_TITLE);
        todoEntry.setPercentDone(DEFAULT_PERCENT_DONE);
        todoEntry.setDeadLine(DEFAULT_DEAD_LINE);
    }

    @Test
    @Transactional
    public void createTodoEntry() throws Exception {
        int databaseSizeBeforeCreate = todoEntryRepository.findAll().size();

        // Create the TodoEntry

        restTodoEntryMockMvc.perform(post("/api/todo-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todoEntry)))
                .andExpect(status().isCreated());

        // Validate the TodoEntry in the database
        List<TodoEntry> todoEntries = todoEntryRepository.findAll();
        assertThat(todoEntries).hasSize(databaseSizeBeforeCreate + 1);
        TodoEntry testTodoEntry = todoEntries.get(todoEntries.size() - 1);
        assertThat(testTodoEntry.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTodoEntry.getPercentDone()).isEqualTo(DEFAULT_PERCENT_DONE);
        assertThat(testTodoEntry.getDeadLine()).isEqualTo(DEFAULT_DEAD_LINE);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = todoEntryRepository.findAll().size();
        // set the field null
        todoEntry.setTitle(null);

        // Create the TodoEntry, which fails.

        restTodoEntryMockMvc.perform(post("/api/todo-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(todoEntry)))
                .andExpect(status().isBadRequest());

        List<TodoEntry> todoEntries = todoEntryRepository.findAll();
        assertThat(todoEntries).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTodoEntries() throws Exception {
        // Initialize the database
        todoEntryRepository.saveAndFlush(todoEntry);

        // Get all the todoEntries
        restTodoEntryMockMvc.perform(get("/api/todo-entries?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(todoEntry.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].percentDone").value(hasItem(DEFAULT_PERCENT_DONE)))
                .andExpect(jsonPath("$.[*].deadLine").value(hasItem(DEFAULT_DEAD_LINE.toString())));
    }

    @Test
    @Transactional
    public void getTodoEntry() throws Exception {
        // Initialize the database
        todoEntryRepository.saveAndFlush(todoEntry);

        // Get the todoEntry
        restTodoEntryMockMvc.perform(get("/api/todo-entries/{id}", todoEntry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(todoEntry.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.percentDone").value(DEFAULT_PERCENT_DONE))
            .andExpect(jsonPath("$.deadLine").value(DEFAULT_DEAD_LINE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTodoEntry() throws Exception {
        // Get the todoEntry
        restTodoEntryMockMvc.perform(get("/api/todo-entries/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTodoEntry() throws Exception {
        // Initialize the database
        todoEntryRepository.saveAndFlush(todoEntry);
        int databaseSizeBeforeUpdate = todoEntryRepository.findAll().size();

        // Update the todoEntry
        TodoEntry updatedTodoEntry = new TodoEntry();
        updatedTodoEntry.setId(todoEntry.getId());
        updatedTodoEntry.setTitle(UPDATED_TITLE);
        updatedTodoEntry.setPercentDone(UPDATED_PERCENT_DONE);
        updatedTodoEntry.setDeadLine(UPDATED_DEAD_LINE);

        restTodoEntryMockMvc.perform(put("/api/todo-entries")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTodoEntry)))
                .andExpect(status().isOk());

        // Validate the TodoEntry in the database
        List<TodoEntry> todoEntries = todoEntryRepository.findAll();
        assertThat(todoEntries).hasSize(databaseSizeBeforeUpdate);
        TodoEntry testTodoEntry = todoEntries.get(todoEntries.size() - 1);
        assertThat(testTodoEntry.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTodoEntry.getPercentDone()).isEqualTo(UPDATED_PERCENT_DONE);
        assertThat(testTodoEntry.getDeadLine()).isEqualTo(UPDATED_DEAD_LINE);
    }

    @Test
    @Transactional
    public void deleteTodoEntry() throws Exception {
        // Initialize the database
        todoEntryRepository.saveAndFlush(todoEntry);
        int databaseSizeBeforeDelete = todoEntryRepository.findAll().size();

        // Get the todoEntry
        restTodoEntryMockMvc.perform(delete("/api/todo-entries/{id}", todoEntry.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<TodoEntry> todoEntries = todoEntryRepository.findAll();
        assertThat(todoEntries).hasSize(databaseSizeBeforeDelete - 1);
    }
}
