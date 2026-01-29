package pl.feature.toggle.service.configuration.environment.infrastructure.in.internal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;

@Configuration("internalConfig")
class Config {

    @Bean
    EnvironmentStatusCascadePort environmentStatusCascadePort(EnvironmentRepository environmentRepository) {
        return new EnvironmentStatusCascadeAdapter(environmentRepository);
    }

}
