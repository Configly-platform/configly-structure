package com.configly.structure.project.infrastructure.out.db;

import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("projectDatabaseConfig")
class Config {

    @Bean
    ProjectQueryRepository projectQueryRepository(final DSLContext dslContext) {
        return new ProjectQueryJooqRepository(dslContext);
    }

    @Bean
    ProjectCommandRepository projectCommandRepository(final DSLContext dslContext) {
        return new ProjectCommandJooqRepository(dslContext);
    }
}
