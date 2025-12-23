package pl.feature.toggle.service.configuration.project.infrastructure.out.db;

import pl.feature.toggle.service.configuration.project.domain.Project;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.feature.toggle.service.model.project.ProjectDescription;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.model.project.ProjectName;
import pl.feature.toggle.service.tables.records.ProjectsRecord;

import java.time.OffsetDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class ProjectMapper {


    static ProjectsRecord toRecord(Project project) {
        ProjectsRecord record = new ProjectsRecord();
        record.setId(project.id().uuid());
        record.setName(project.name().value());
        record.setDescription(project.description().value());
        record.setCreatedAt(OffsetDateTime.now());
        return record;
    }

    static Project toDomain(ProjectsRecord record) {
        return Project.create(
                ProjectId.create(record.getId()),
                ProjectName.create(record.getName()),
                ProjectDescription.create(record.getDescription())
        );
    }


}
