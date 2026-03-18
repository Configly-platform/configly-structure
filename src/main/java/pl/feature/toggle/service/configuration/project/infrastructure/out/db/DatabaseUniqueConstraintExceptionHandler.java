package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jooq.exception.IntegrityConstraintViolationException;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;

import java.util.function.Supplier;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class DatabaseUniqueConstraintExceptionHandler {

    static Integer translateUniqueConstraintException(Supplier<Integer> action, Project project) {
        try {
            return action.get();
        } catch (IntegrityConstraintViolationException e) {
            throw new ProjectAlreadyExistsException(project.name());
        }
    }
}
