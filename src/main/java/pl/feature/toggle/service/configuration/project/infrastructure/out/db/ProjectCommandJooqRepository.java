package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectCommandRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectUpdateResult;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectUpdateFailedException;

import static pl.feature.toggle.service.configuration.project.infrastructure.out.db.DatabaseUniqueConstraintExceptionHandler.translateUniqueConstraintException;
import static pl.feature.toggle.service.configuration.project.infrastructure.out.db.ProjectMapper.toRecord;
import static pl.feature.toggle.service.tables.Projects.PROJECTS;

@AllArgsConstructor
final class ProjectCommandJooqRepository implements ProjectCommandRepository {

    private final DSLContext dslContext;

    @Override
    public void save(Project project) {
        translateUniqueConstraintException(
                () -> dslContext.insertInto(PROJECTS)
                        .set(toRecord(project))
                        .execute(),
                project
        );
    }

    @Override
    public void update(ProjectUpdateResult updateResult) {
        var project = updateResult.project();
        var updatedRows = translateUniqueConstraintException(
                () -> dslContext.update(PROJECTS)
                        .set(toRecord(project))
                        .where(PROJECTS.ID.eq(project.id().uuid()))
                        .and(PROJECTS.REVISION.eq(updateResult.expectedRevision().value()))
                        .execute(),
                project
        );
        if (updatedRows == 0) {
            throw new ProjectUpdateFailedException(updateResult);
        }
    }
}
