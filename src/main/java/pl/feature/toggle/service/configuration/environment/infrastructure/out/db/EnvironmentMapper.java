package pl.feature.toggle.service.configuration.environment.infrastructure.out.db;

import pl.feature.toggle.service.configuration.environment.domain.Environment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.feature.toggle.service.model.environment.EnvironmentId;
import pl.feature.toggle.service.model.environment.EnvironmentName;
import pl.feature.toggle.service.model.project.ProjectId;
import pl.feature.toggle.service.tables.records.EnvironmentsRecord;

import java.time.OffsetDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EnvironmentMapper {

    static EnvironmentsRecord toRecord(Environment environment) {
        final var record = new EnvironmentsRecord();
        record.setId(environment.id().uuid());
        record.setName(environment.name().value());
        record.setCreatedAt(OffsetDateTime.now());
        record.setProjectId(environment.projectId().uuid());
        return record;
    }

    static Environment toDomain(EnvironmentsRecord record) {
        return Environment.create(
                EnvironmentId.create(record.getId()),
                ProjectId.create(record.getProjectId()),
                EnvironmentName.create(record.getName())
        );
    }

}
