package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.UpdatedAt;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.model.project.ProjectStatus;
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
