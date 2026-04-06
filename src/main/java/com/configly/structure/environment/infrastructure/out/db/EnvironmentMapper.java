package com.configly.structure.environment.infrastructure.out.db;

import com.configly.structure.tables.records.EnvironmentsRecord;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import com.configly.structure.environment.domain.Environment;
import com.configly.structure.environment.domain.EnvironmentType;
import com.configly.model.CreatedAt;
import com.configly.model.Revision;
import com.configly.model.UpdatedAt;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.environment.EnvironmentName;
import com.configly.model.environment.EnvironmentStatus;
import com.configly.model.project.ProjectId;

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
