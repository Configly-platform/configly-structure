package com.configly.structure.environment.infrastructure.in.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.configly.structure.environment.application.port.out.EnvironmentCommandRepository;
import com.configly.structure.project.application.port.out.environment.EnvironmentStatusCascadePort;

@Configuration("internalConfig")
class Config {

    @Bean
    EnvironmentStatusCascadePort environmentStatusCascadePort(EnvironmentCommandRepository environmentCommandRepository) {
        return new EnvironmentStatusCascadeAdapter(environmentCommandRepository);
    }

}
