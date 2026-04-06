package com.configly.structure;

import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.configly.structure.tables.Environments.ENVIRONMENTS;
import static com.configly.structure.tables.Projects.PROJECTS;
import static pl.feature.ftaas.outbox.jooq.tables.OutboxEvents.OUTBOX_EVENTS;


@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
public abstract class AbstractITTest {

    @DynamicPropertySource
    static void pgProps(DynamicPropertyRegistry r) {
        var pg = PostgresContainer.getInstance();
        r.add("spring.datasource.url", pg::getJdbcUrl);
        r.add("spring.datasource.username", pg::getUsername);
        r.add("spring.datasource.password", pg::getPassword);
    }

    @Autowired
    private DSLContext dslContext;

    @AfterEach
    void tearDown() {
        clearOutbox();
        clearEnvironments();
        clearProjects();
    }

    private void clearEnvironments() {
        dslContext.deleteFrom(ENVIRONMENTS).execute();
    }

    private void clearProjects() {
        dslContext.deleteFrom(PROJECTS).execute();
    }

    protected void clearOutbox() {
        dslContext.deleteFrom(OUTBOX_EVENTS).execute();
    }

}
