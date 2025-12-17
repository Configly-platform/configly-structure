package pl.feature.toggle.service.project.infrastructure.out.db;

import github.saqie.ftaas.jooq.tables.records.ProjectsRecord;
import pl.feature.toggle.service.project.domain.Project;
import com.ftaas.domain.project.ProjectDescription;
import com.ftaas.domain.project.ProjectId;
import com.ftaas.domain.project.ProjectName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
