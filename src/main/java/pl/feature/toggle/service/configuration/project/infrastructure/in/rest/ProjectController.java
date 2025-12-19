package pl.feature.toggle.service.configuration.project.infrastructure.in.rest;

import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectCommand;
import pl.feature.toggle.service.configuration.project.application.port.in.CreateProjectUseCase;
import pl.feature.toggle.service.configuration.project.infrastructure.in.rest.dto.CreateProjectDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/rest/api/projects")
@AllArgsConstructor
final class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;

    @PostMapping
    UUID createProject(@RequestBody @Valid CreateProjectDto dto) {
        var command = CreateProjectCommand.from(dto);
        return createProjectUseCase.handle(command).uuid();
    }

}
