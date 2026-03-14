package pl.feature.toggle.service.configuration.environment.infrastructure.in.internal;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.CascadedEnvironmentStatusChange;
import pl.feature.toggle.service.configuration.project.application.port.out.environment.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.model.project.ProjectId;

import java.util.List;

@AllArgsConstructor
class EnvironmentStatusCascadeAdapter implements EnvironmentStatusCascadePort {

    private final EnvironmentCommandRepository environmentCommandRepository;

    @Override
    public List<CascadedEnvironmentStatusChange> archiveCascadeByProjectId(ProjectId projectId) {
        return environmentCommandRepository.archiveAllByProjectId(projectId);
    }

}
