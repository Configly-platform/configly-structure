package pl.feature.toggle.service.configuration.project.application.policy;

import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import pl.feature.toggle.service.model.project.ProjectName;

@AllArgsConstructor
class ProjectNameUniquePolicy {

    private final ProjectQueryRepository projectQueryRepository;

    void ensure(ProjectName projectName) {
        boolean exists = projectQueryRepository.existsByName(projectName);
        if (exists) {
            throw new ProjectAlreadyExistsException(projectName);
        }
    }

}
