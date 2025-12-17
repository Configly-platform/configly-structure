package github.saqie.projects.environment.infrastructure.out.db;

import com.ftaas.domain.environment.EnvironmentId;
import github.saqie.ftaas.jooq.tables.records.EnvironmentsRecord;
import github.saqie.projects.environment.domain.Environment;
import com.ftaas.domain.environment.EnvironmentName;
import com.ftaas.domain.project.ProjectId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
