package pl.feature.toggle.service.configuration.project.infrastructure.in.rest;

import org.springframework.web.bind.annotation.*;
import pl.feature.toggle.service.configuration.project.application.port.in.ChangeProjectStatusUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.UpdateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.ChangeProjectStatusCommand;
import pl.feature.toggle.service.configuration.project.application.port.in.command.CreateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.application.port.in.command.UpdateProjectCommand;
import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.ProjectSnapshotDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.UUID;

@RestController
@RequestMapping("/rest/api/projects")
@AllArgsConstructor
final class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    private final UpdateProjectUseCase updateProjectUseCase;
    private final ChangeProjectStatusUseCase changeProjectStatusUseCase;

    @PostMapping
    UUID createProject(@RequestBody @Valid ProjectSnapshotDto dto) {
        var command = CreateProjectCommand.from(dto);
        return createProjectUseCase.handle(command).uuid();
    }

    @PutMapping("/{projectId}")
    void updateProject(@PathVariable String projectId, @RequestBody @Valid ProjectSnapshotDto dto) {
        updateProjectUseCase.handle(UpdateProjectCommand.from(projectId, dto));
    }

    @PatchMapping("/{projectId}/status")
    void changeStatus(@PathVariable String projectId, @RequestBody @Valid String status){
        changeProjectStatusUseCase.handle(ChangeProjectStatusCommand.of(projectId,status));
    }

}
