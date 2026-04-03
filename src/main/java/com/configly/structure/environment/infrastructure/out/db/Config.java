package com.configly.structure.environment.infrastructure.out.db;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;

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
