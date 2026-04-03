package com.configly.structure.project.infrastructure.in.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.configly.structure.project.application.port.out.ProjectQueryRepository;
import com.configly.structure.project.infrastructure.in.rest.dto.internal.ProjectRefResponse;
import com.configly.structure.project.infrastructure.in.rest.dto.internal.ProjectViewResponse;
import com.configly.model.project.ProjectId;

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
