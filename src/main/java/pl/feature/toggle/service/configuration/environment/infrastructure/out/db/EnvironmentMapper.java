package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import pl.feature.toggle.service.configuration.environment.domain.EnvironmentType;
import pl.feature.toggle.service.model.CreatedAt;
import pl.feature.toggle.service.model.Revision;
import pl.feature.toggle.service.model.UpdatedAt;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.environment.EnvironmentStatus;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.tables.records.EnvironmentsRecord;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EnvironmentMapper {

    static EnvironmentsRecord toRecord(Environment environment) {
        final var record = new EnvironmentsRecord();
        record.setId(environment.id().uuid());
        record.setName(environment.name().value());
        record.setCreatedAt(environment.createdAt().toLocalDateTime());
        record.setProjectId(environment.projectId().uuid());
        record.setStatus(environment.status().name());
        record.setType(environment.type().name());
        record.setRevision(environment.revision().value());
        record.setUpdatedAt(environment.updatedAt().toLocalDateTime());
        return record;
    }

    static Environment toDomain(EnvironmentsRecord record) {
        return new Environment(
                EnvironmentId.create(record.getId()),
                ProjectId.create(record.getProjectId()),
                EnvironmentName.create(record.getName()),
                EnvironmentType.valueOf(record.getType()),
                EnvironmentStatus.valueOf(record.getStatus()),
                Revision.from(record.getRevision()),
                CreatedAt.of(record.getCreatedAt()),
                UpdatedAt.of(record.getUpdatedAt())
        );
    }

}
