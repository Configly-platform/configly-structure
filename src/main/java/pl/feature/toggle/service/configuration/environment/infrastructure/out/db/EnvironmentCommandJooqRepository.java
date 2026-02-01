package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentStatus;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentNotFoundException;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.project.ProjectId;

import static pl.feature.toggle.service.configuration.environment.infrastructure.out.db.EnvironmentMapper.toRecord;
import static pl.feature.toggle.service.tables.Environments.ENVIRONMENTS;

@AllArgsConstructor
class EnvironmentCommandJooqRepository implements EnvironmentCommandRepository {

    private final DSLContext dsl;

    @Override
    public EnvironmentId save(Environment environment) {
        var rows = dsl.insertInto(ENVIRONMENTS)
                .set(toRecord(environment))
                .onConflict(ENVIRONMENTS.PROJECT_ID, ENVIRONMENTS.NAME)
                .doNothing()
                .execute();

        if (rows == 0) {
            throw new EnvironmentAlreadyExistsException(environment.name(), environment.projectId());
        }
        return environment.id();
    }

    @Override
    public void archiveAllByProjectId(ProjectId projectId) {
        dsl.update(ENVIRONMENTS)
                .set(ENVIRONMENTS.STATUS, EnvironmentStatus.ARCHIVED.name())
                .where(ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid()))
                .execute();
    }

    @Override
    public void restoreAllByProjectId(ProjectId projectId) {
        dsl.update(ENVIRONMENTS)
                .set(ENVIRONMENTS.STATUS, EnvironmentStatus.ACTIVE.name())
                .where(ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid()))
                .execute();
    }

    @Override
    public void update(Environment environment) {
        var rows = dsl.update(ENVIRONMENTS)
                .set(toRecord(environment))
                .where(ENVIRONMENTS.ID.eq(environment.id().uuid()))
                .execute();
        if (rows == 0) {
            throw new EnvironmentNotFoundException(environment.id(), environment.projectId());
        }
    }


}
