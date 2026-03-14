package pl.feature.toggle.service.configuration.environment.infrastructure.in.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;

@Configuration("internalConfig")
class Config {

    @Bean
    EnvironmentStatusCascadePort environmentStatusCascadePort(EnvironmentCommandRepository environmentCommandRepository) {
        return new EnvironmentStatusCascadeAdapter(environmentCommandRepository);
    }

}
