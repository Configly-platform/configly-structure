package com.configly.structure.project.infrastructure.in.rest;

import org.springframework.web.bind.annotation.*;
import com.configly.structure.project.application.port.in.ChangeProjectStatusUseCase;
import com.configly.structure.project.application.port.in.UpdateProjectUseCase;
import com.configly.structure.project.application.port.in.command.ChangeProjectStatusCommand;
import com.configly.structure.project.application.port.in.command.CreateProjectCommand;
import com.configly.structure.project.application.port.in.CreateProjectUseCase;
import com.configly.structure.project.application.port.in.command.UpdateProjectCommand;
import com.configly.structure.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.configly.web.model.actor.ActorProvider;
import com.configly.web.model.correlation.CorrelationProvider;

import java.util.UUID;

@RestController
@RequestMapping("/rest/api/projects")
@AllArgsConstructor
final class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final ChangeProjectStatusUseCase changeProjectStatusUseCase;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;

    @PostMapping
    UUID createProject(@RequestBody @Valid ProjectSnapshotDto dto) {
        var command = CreateProjectCommand.from(dto, actorProvider.current(), correlationProvider.current());
        return createProjectUseCase.handle(command).uuid();
    }

    @PutMapping("/{projectId}")
    void updateProject(@PathVariable String projectId, @RequestBody @Valid ProjectSnapshotDto dto) {
        var command = UpdateProjectCommand.from(projectId, dto, actorProvider.current(), correlationProvider.current());
        updateProjectUseCase.handle(command);
    }

    @PatchMapping("/{projectId}/status")
    void changeStatus(@PathVariable String projectId, @RequestBody @Valid String status) {
        var command = ChangeProjectStatusCommand.of(projectId, status, actorProvider.current(), correlationProvider.current());
        changeProjectStatusUseCase.handle(command);
    }

}
