package pl.feature.toggle.service.configuration.project.infrastructure.in.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.feature.toggle.service.configuration.project.application.port.out.ProjectQueryRepository;
import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.internal.ProjectRefResponse;
import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.internal.ProjectViewResponse;
import pl.feature.toggle.service.model.project.ProjectId;

@RestController
@RequestMapping("/internal/projects")
@AllArgsConstructor
class InternalProjectController {

    private final ProjectQueryRepository projectQueryRepository;

    @GetMapping("/{projectId}/reference")
    ProjectRefResponse getProjectReference(@PathVariable String projectId) {
        var project = projectQueryRepository.getOrThrow(ProjectId.create(projectId));
        return new ProjectRefResponse(project.id().idAsString(), project.status().name(), project.revision().value());
    }

    @GetMapping("/{projectId}/view")
    ProjectViewResponse getProjectView(@PathVariable String projectId) {
        var project = projectQueryRepository.getOrThrow(ProjectId.create(projectId));
        return new ProjectViewResponse(
                project.id().idAsString(),
                project.name().value(),
                project.description().value(),
                project.status().name(),
                project.createdAt().toLocalDateTime(),
                project.updatedAt().toLocalDateTime(),
                project.revision().value()
        );
    }
}
