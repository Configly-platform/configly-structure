package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;

@Configuration("environmentDatabaseConfig")
class Config {

    @Bean
    EnvironmentQueryRepository environmentQueryRepository(DSLContext dslContext) {
        return new EnvironmentQueryJooqRepository(dslContext);
    }

    @Bean
    EnvironmentCommandRepository environmentCommandRepository(DSLContext dslContext) {
        return new EnvironmentCommandJooqRepository(dslContext);
    }

}
