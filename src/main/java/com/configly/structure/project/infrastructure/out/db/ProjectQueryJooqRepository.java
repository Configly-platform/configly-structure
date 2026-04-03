package com.configly.structure.project.infrastructure.out.db;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.domain.Project;
import com.configly.structure.project.domain.exception.ProjectNotFoundException;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectStatus;

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
