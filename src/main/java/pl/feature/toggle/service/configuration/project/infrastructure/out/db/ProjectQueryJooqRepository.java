package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.ProjectStatus;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectNotFoundException;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;

import java.util.Optional;

import static pl.feature.toggle.service.tables.Projects.PROJECTS;

@AllArgsConstructor
class ProjectQueryJooqRepository implements ProjectQueryRepository {

    private final DSLContext dslContext;

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
    public Project getOrThrow(ProjectId projectId) {
        return dslContext.selectFrom(PROJECTS)
                .where(PROJECTS.ID.eq(projectId.uuid()))
                .fetchOptional()
                .map(ProjectMapper::toDomain)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    @Override
    public ProjectStatus fetchStatus(ProjectId projectId) {
        return dslContext.select(PROJECTS.STATUS)
                .from(PROJECTS)
                .where(PROJECTS.ID.eq(projectId.uuid()))
                .fetchOptional()
                .map(record -> ProjectStatus.valueOf(record.value1()))
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }
}
