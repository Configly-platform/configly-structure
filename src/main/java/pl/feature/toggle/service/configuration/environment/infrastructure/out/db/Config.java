package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("environmentDatabaseConfig")
class Config {

    @Bean
    EnvironmentRepository environmentRepository(DSLContext dslContext) {
        return new EnvironmentJooqRepository(dslContext);
    }

}
