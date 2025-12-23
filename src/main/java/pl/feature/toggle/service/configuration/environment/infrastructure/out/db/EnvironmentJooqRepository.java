package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.Optional;

import static pl.feature.toggle.service.configuration.environment.infrastructure.out.db.EnvironmentMapper.toRecord;
import static pl.feature.toggle.service.tables.Environments.ENVIRONMENTS;

@AllArgsConstructor
final class EnvironmentJooqRepository implements EnvironmentRepository {

    private final DSLContext dsl;

    @Override
    public EnvironmentId save(Environment environment) {
        var rows = dsl.insertInto(ENVIRONMENTS)
                .set(toRecord(environment))
                .onConflict(ENVIRONMENTS.PROJECT_ID, ENVIRONMENTS.NAME)
                .doNothing()
                .execute();

        if (rows == 0) {
            throw new EnvironmentAlreadyExistsException(environment);
        }
        return environment.id();
    }

    @Override
    public Optional<Environment> findById(final EnvironmentId environmentId) {
        return dsl.selectFrom(ENVIRONMENTS)
                .where(ENVIRONMENTS.ID.eq(environmentId.uuid()))
                .fetchOptional()
                .map(EnvironmentMapper::toDomain);
    }

    @Override
    public boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName) {
        return dsl.fetchExists(ENVIRONMENTS, ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid()), ENVIRONMENTS.NAME.eq(environmentName.value()));
    }

    @Override
    public boolean exists(EnvironmentId environmentId) {
        return dsl.fetchExists(ENVIRONMENTS, ENVIRONMENTS.ID.eq(environmentId.uuid()));
    }
}
