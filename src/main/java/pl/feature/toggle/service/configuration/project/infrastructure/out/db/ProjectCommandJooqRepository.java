package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectUpdateFailedException;

import static pl.feature.toggle.service.configuration.project.infrastructure.out.db.ProjectMapper.toRecord;
import static pl.feature.toggle.service.tables.Projects.PROJECTS;

@AllArgsConstructor
final class ProjectCommandJooqRepository implements ProjectCommandRepository {

    private final DSLContext dslContext;

    @Override
    public void save(Project project) {
        var rows = dslContext.insertInto(PROJECTS)
                .set(toRecord(project))
                .onConflict(PROJECTS.NAME)
                .doNothing()
                .execute();
        if (rows == 0) {
            throw new ProjectAlreadyExistsException(project.name());
        }

    }

    @Override
    public void update(ProjectUpdateResult updateResult) {
        var project = updateResult.project();
        var rows = dslContext.update(PROJECTS)
                .set(toRecord(project))
                .where(PROJECTS.ID.eq(project.id().uuid()))
                .and(PROJECTS.REVISION.eq(updateResult.expectedRevision().value()))
                .execute();
        if (rows == 0) {
            throw new ProjectUpdateFailedException(updateResult);
        }
    }
}
