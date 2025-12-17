package github.saqie.projects.environment.infrastructure.in.rest;

import github.saqie.projects.environment.application.port.in.CreateEnvironmentCommand;
import github.saqie.projects.environment.application.port.in.CreateEnvironmentUseCase;
import github.saqie.projects.environment.infrastructure.in.rest.dto.CreateEnvironmentDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/rest/api/environments")
final class EnvironmentController {

    private final CreateEnvironmentUseCase createEnvironmentUseCase;

    @PostMapping
    UUID createEnvironment(@RequestBody @Valid CreateEnvironmentDto dto) {
        var command = CreateEnvironmentCommand.from(dto);
        return createEnvironmentUseCase.handle(command).uuid();
    }

}
