package com.configly.structure.project.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jooq.exception.IntegrityConstraintViolationException;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.exception.ProjectAlreadyExistsException;

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
