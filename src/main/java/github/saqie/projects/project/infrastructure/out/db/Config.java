package github.saqie.projects.project.infrastructure.out.db;

import github.saqie.projects.project.application.port.out.ProjectRepository;
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
