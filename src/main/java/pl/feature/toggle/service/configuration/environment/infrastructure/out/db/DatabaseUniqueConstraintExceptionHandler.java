package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jooq.exception.IntegrityConstraintViolationException;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;

import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class DatabaseUniqueConstraintExceptionHandler {

    static Integer translateUniqueConstraintException(Supplier<Integer> action, Environment environment) {
        try {
            return action.get();
        } catch (IntegrityConstraintViolationException e) {
            throw new EnvironmentAlreadyExistsException(environment.name(), environment.projectId());
        }
    }
}
