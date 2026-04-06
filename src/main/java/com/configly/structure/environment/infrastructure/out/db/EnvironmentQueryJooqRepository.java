package com.configly.structure.environment.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.exception.EnvironmentNotFoundException;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

import static com.configly.structure.tables.Environments.ENVIRONMENTS;

@AllArgsConstructor
final class EnvironmentQueryJooqRepository implements EnvironmentQueryRepository {

    private final DSLContext dsl;

    @Override
    public Environment getOrThrow(ProjectId projectId, EnvironmentId environmentId) {
        return dsl.selectFrom(ENVIRONMENTS)
                .where(ENVIRONMENTS.ID.eq(environmentId.uuid()))
                .and(ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid()))
                .fetchOptional()
                .map(EnvironmentMapper::toDomain)
                .orElseThrow(() -> new EnvironmentNotFoundException(environmentId, projectId));
    }

}
