package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.Optional;

import static pl.feature.toggle.service.configuration.project.infrastructure.out.db.ProjectMapper.toRecord;
import static pl.feature.toggle.service.tables.Projects.PROJECTS;

@AllArgsConstructor
final class ProjectJooqRepository implements ProjectRepository {

    private final DSLContext dslContext;

    @Override
    public ProjectId save(Project project) {
        var rows = dslContext.insertInto(PROJECTS)
                .set(toRecord(project))
                .onConflict(PROJECTS.NAME)
                .doNothing()
                .execute();
        if (rows == 0) {
            throw new ProjectAlreadyExistsException(project.name());
        }

        return project.id();
    }

    @Override
    public boolean exists(ProjectId projectId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(PROJECTS)
                        .where(PROJECTS.ID.eq(projectId.uuid()))
        );
    }

    @Override
    public boolean existsByName(ProjectName name) {
        return dslContext.fetchExists(PROJECTS, PROJECTS.NAME.eq(name.value()));
    }

    @Override
    public Optional<Project> findById(final ProjectId projectId) {
        return dslContext.selectFrom(PROJECTS)
                .where(PROJECTS.ID.eq(projectId.uuid()))
                .fetchOptional()
                .map(ProjectMapper::toDomain);
    }
}
