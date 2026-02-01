package pl.feature.toggle.service.configuration.environment.infrastructure.in.internal;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentCommandRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.model.project.ProjectId;

@AllArgsConstructor
class EnvironmentStatusCascadeAdapter implements EnvironmentStatusCascadePort {

    private final EnvironmentCommandRepository environmentCommandRepository;

    @Override
    public void archiveCascadeByProjectId(ProjectId projectId) {
        environmentCommandRepository.archiveAllByProjectId(projectId);
    }

    @Override
    public void restoreCascadeByProjectId(ProjectId projectId) {
        environmentCommandRepository.restoreAllByProjectId(projectId);
    }
}
