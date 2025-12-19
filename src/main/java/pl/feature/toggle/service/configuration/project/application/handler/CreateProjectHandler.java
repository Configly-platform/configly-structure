package pl.feature.toggle.service.configuration.project.application.handler;

import com.ftaas.domain.project.ProjectId;
import github.saqie.ftaasoutbox.api.OutboxWriter;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectRepository;
import pl.feature.toggle.service.configuration.project.domain.Project;
import pl.feature.toggle.service.configuration.project.domain.exception.ProjectAlreadyExistsException;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import static com.ftaas.contracts.topic.KafkaTopic.PROJECT_ENV;
import static pl.feature.toggle.service.configuration.project.application.handler.ProjectHandlerEventMapper.createProjectCreatedEvent;

@AllArgsConstructor
class CreateProjectHandler implements CreateProjectUseCase {

    ProjectRepository projectRepository;
    OutboxWriter outboxWriter;

    @Override
    @Transactional
    public ProjectId handle(CreateProjectCommand command) {
        var project = Project.create(command);

        validate(project);

        var projectId = projectRepository.save(project);

        var event = createProjectCreatedEvent(project);
        outboxWriter.write(event, PROJECT_ENV.topic());

        return projectId;
    }

    private void validate(Project project) {
        validateUniqueName(project);
    }

    private void validateUniqueName(Project project) {
        if (projectRepository.existsByName(project.name())) {
            throw new ProjectAlreadyExistsException(project.name());
        }
    }

}
