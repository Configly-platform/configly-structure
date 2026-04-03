package com.configly.structure.project.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.configly.structure.project.domain.Project;
import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.UpdatedAt;
import com.configly.model.project.ProjectDescription;
import com.configly.model.project.ProjectId;
import com.configly.model.project.ProjectName;
import com.configly.model.project.ProjectStatus;
import pl.feature.toggle.service.tables.records.ProjectsRecord;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ProjectMapper {


    static ProjectsRecord toRecord(Project project) {
        ProjectsRecord record = new ProjectsRecord();
        record.setId(project.id().uuid());
        record.setName(project.name().value());
        record.setDescription(project.description().value());
        record.setCreatedAt(project.createdAt().toLocalDateTime());
        record.setUpdatedAt(project.updatedAt().toLocalDateTime());
        record.setStatus(project.status().name());
        record.setRevision(project.revision().value());
        return record;
    }

    static Project toDomain(ProjectsRecord record) {
        return new Project(
                ProjectId.create(record.getId()),
                ProjectName.create(record.getName()),
                ProjectDescription.create(record.getDescription()),
                ProjectStatus.valueOf(record.getStatus()),
                Revision.from(record.getRevision()),
                CreatedAt.of(record.getCreatedAt()),
                UpdatedAt.of(record.getUpdatedAt())
        );
    }


}
