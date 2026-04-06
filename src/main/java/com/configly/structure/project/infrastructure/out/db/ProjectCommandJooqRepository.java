package com.configly.structure.project.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import com.configly.structure.project.application.port.out.ProjectCommandRepository;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.ProjectUpdateResult;
import com.configly.structure.project.domain.exception.ProjectUpdateFailedException;

import static com.configly.structure.project.infrastructure.out.db.DatabaseUniqueConstraintExceptionHandler.translateUniqueConstraintException;
import static com.configly.structure.project.infrastructure.out.db.ProjectMapper.toRecord;
import static com.configly.structure.tables.Projects.PROJECTS;

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
