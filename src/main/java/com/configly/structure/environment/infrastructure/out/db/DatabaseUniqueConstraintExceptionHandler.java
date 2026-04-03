package com.configly.structure.environment.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jooq.exception.IntegrityConstraintViolationException;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.exception.EnvironmentAlreadyExistsException;

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
