package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentQueryRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentNotFoundException;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import static pl.feature.toggle.service.tables.Environments.ENVIRONMENTS;

@AllArgsConstructor
final class EnvironmentQueryJooqRepository implements EnvironmentQueryRepository {

    private final DSLContext dsl;

    @Override
    public Environment getOrThrow(EnvironmentId environmentId, ProjectId projectId) {
        return dsl.selectFrom(ENVIRONMENTS)
                .where(ENVIRONMENTS.ID.eq(environmentId.uuid()))
                .and(ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid()))
                .fetchOptional()
                .map(EnvironmentMapper::toDomain)
                .orElseThrow(() -> new EnvironmentNotFoundException(environmentId, projectId));
    }

    @Override
    public boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName) {
        return dsl.fetchExists(ENVIRONMENTS, ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid()), ENVIRONMENTS.NAME.eq(environmentName.value()));
    }
}
