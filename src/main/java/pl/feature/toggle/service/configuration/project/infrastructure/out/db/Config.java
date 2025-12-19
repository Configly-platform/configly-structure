package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("projectDatabaseConfig")
class Config {

    @Bean
    ProjectRepository projectRepository(final DSLContext dslContext) {
        return new ProjectJooqRepository(dslContext);
    }
}
