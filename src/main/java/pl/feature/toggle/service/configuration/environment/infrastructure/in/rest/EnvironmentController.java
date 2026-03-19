package pl.feature.toggle.service.configuration.environment.infrastructure.in.rest;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.*;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentStatusUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.ChangeEnvironmentTypeUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.UpdateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentStatusCommand;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.ChangeEnvironmentTypeCommand;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.CreateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.application.port.in.CreateEnvironmentUseCase;
import pl.feature.toggle.service.configuration.environment.application.port.in.command.UpdateEnvironmentCommand;
import pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import pl.feature.toggle.service.configuration.environment.infrastructure.in.rest.dto.UpdateEnvironmentDto;
import pl.feature.toggle.service.web.actor.ActorProvider;
import pl.feature.toggle.service.web.correlation.CorrelationProvider;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/api/projects")
final class EnvironmentController {

    private final CreateEnvironmentUseCase createEnvironmentUseCase;
    private final UpdateEnvironmentUseCase updateEnvironmentUseCase;
    private final ChangeEnvironmentStatusUseCase changeEnvironmentStatusUseCase;
    private final ChangeEnvironmentTypeUseCase changeEnvironmentTypeUseCase;
    private final ActorProvider actorProvider;
    private final CorrelationProvider correlationProvider;

    @PostMapping("/{projectId}/environments")
    UUID createEnvironment(@PathVariable UUID projectId, @RequestBody @Valid CreateEnvironmentDto dto) {
        var command = CreateEnvironmentCommand.from(projectId, dto, actorProvider.current(), correlationProvider.current());
        return createEnvironmentUseCase.handle(command).uuid();
    }

    @PutMapping("/{projectId}/environments/{environmentId}")
    void updateEnvironment(@PathVariable UUID projectId, @PathVariable UUID environmentId, @RequestBody @Valid UpdateEnvironmentDto dto) {
        var command = UpdateEnvironmentCommand.of(projectId, environmentId, dto, actorProvider.current(), correlationProvider.current());
        updateEnvironmentUseCase.handle(command);
    }

    @PatchMapping("/{projectId}/environments/{environmentId}/status")
    void changeStatus(@PathVariable UUID projectId, @PathVariable UUID environmentId, @RequestBody @NotEmpty String status) {
        var command = ChangeEnvironmentStatusCommand.of(projectId, environmentId, status, actorProvider.current(), correlationProvider.current());
        changeEnvironmentStatusUseCase.handle(command);
    }

    @PatchMapping("/{projectId}/environments/{environmentId}/type")
    void changeType(@PathVariable UUID projectId, @PathVariable UUID environmentId, @RequestBody @NotEmpty String type) {
        var command = ChangeEnvironmentTypeCommand.of(projectId, environmentId, type, actorProvider.current(), correlationProvider.current());
        changeEnvironmentTypeUseCase.handle(command);
    }


}
