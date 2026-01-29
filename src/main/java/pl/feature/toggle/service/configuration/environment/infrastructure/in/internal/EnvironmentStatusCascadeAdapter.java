package pl.feature.toggle.service.configuration.environment.infrastructure.in.internal;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.project.application.port.out.EnvironmentStatusCascadePort;
import pl.feature.toggle.service.model.project.ProjectId;

@AllArgsConstructor
class EnvironmentStatusCascadeAdapter implements EnvironmentStatusCascadePort {

    private final EnvironmentRepository environmentRepository;

    @Override
    public void archiveCascadeByProjectId(ProjectId projectId) {
        environmentRepository.archiveAllByProjectId(projectId);
    }

    @Override
    public void restoreCascadeByProjectId(ProjectId projectId) {
        environmentRepository.restoreAllByProjectId(projectId);
    }
}
