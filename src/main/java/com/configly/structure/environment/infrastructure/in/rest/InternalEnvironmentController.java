package com.configly.structure.environment.infrastructure.in.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.configly.structure.environment.application.port.out.EnvironmentQueryRepository;
import com.configly.structure.environment.infrastructure.in.rest.dto.internal.EnvironmentRefResponse;
import com.configly.structure.environment.infrastructure.in.rest.dto.internal.EnvironmentViewResponse;
import com.configly.model.environment.EnvironmentId;
import com.configly.model.project.ProjectId;

@RestController
@RequestMapping("/internal/projects")
@AllArgsConstructor
class InternalEnvironmentController {

    private final EnvironmentQueryRepository environmentQueryRepository;

    @GetMapping("/{projectId}/environments/{environmentId}/reference")
    EnvironmentRefResponse getEnvironmentReference(@PathVariable String projectId, @PathVariable String environmentId) {
        var environment = environmentQueryRepository.getOrThrow(ProjectId.create(projectId), EnvironmentId.create(environmentId));
        return new EnvironmentRefResponse(
                environment.id().idAsString(),
                environment.projectId().idAsString(),
                environment.status().name(),
                environment.revision().value()
        );
    }

    @GetMapping("/{projectId}/environments/{environmentId}/view")
    EnvironmentViewResponse getEnvironmentView(@PathVariable String projectId, @PathVariable String environmentId) {
        var environment = environmentQueryRepository.getOrThrow(ProjectId.create(projectId), EnvironmentId.create(environmentId));
        return new EnvironmentViewResponse(
                environment.id().idAsString(),
                environment.projectId().idAsString(),
                environment.name().value(),
                environment.type().name(),
                environment.status().name(),
                environment.createdAt().toLocalDateTime(),
                environment.updatedAt().toLocalDateTime(),
                environment.revision().value()
        );
    }

}
