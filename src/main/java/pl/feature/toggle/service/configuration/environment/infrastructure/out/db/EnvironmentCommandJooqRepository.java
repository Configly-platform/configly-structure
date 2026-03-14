package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentUpdateResult;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentAlreadyExistsException;
import pl.feature.toggle.service.configuration.environment.domain.exception.EnvironmentUpdateFailedException;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.List;

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
    public List<CascadedEnvironmentStatusChange> archiveAllByProjectId(ProjectId projectId) {
        return dsl.update(ENVIRONMENTS)
                .set(ENVIRONMENTS.STATUS, EnvironmentStatus.ARCHIVED.name())
                .set(ENVIRONMENTS.REVISION, ENVIRONMENTS.REVISION.plus(1))
                .where(
                        ENVIRONMENTS.PROJECT_ID.eq(projectId.uuid())
                                .and(ENVIRONMENTS.STATUS.ne(EnvironmentStatus.ARCHIVED.name()))
                )
                .returning(
                        ENVIRONMENTS.ID,
                        ENVIRONMENTS.PROJECT_ID,
                        ENVIRONMENTS.STATUS,
                        ENVIRONMENTS.REVISION,
                        ENVIRONMENTS.CREATED_AT
                )
                .fetch(record -> new CascadedEnvironmentStatusChange(
                        EnvironmentId.create(record.get(ENVIRONMENTS.ID)),
                        ProjectId.create(record.get(ENVIRONMENTS.PROJECT_ID)),
                        EnvironmentStatus.valueOf(record.get(ENVIRONMENTS.STATUS)),
                        EnvironmentStatus.ACTIVE,
                        Revision.from(record.get(ENVIRONMENTS.REVISION)),
                        CreatedAt.of(record.get(ENVIRONMENTS.CREATED_AT))
                ));
    }

    @Override
    public void update(EnvironmentUpdateResult updateResult) {
        var environment = updateResult.environment();
        var rows = dsl.update(ENVIRONMENTS)
                .set(toRecord(environment))
                .where(ENVIRONMENTS.ID.eq(environment.id().uuid()))
                .and(ENVIRONMENTS.REVISION.eq(updateResult.expectedRevision().value()))
                .execute();
        if (rows == 0) {
            throw new EnvironmentUpdateFailedException(updateResult);
        }
    }


}
