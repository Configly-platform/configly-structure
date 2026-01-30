package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
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
