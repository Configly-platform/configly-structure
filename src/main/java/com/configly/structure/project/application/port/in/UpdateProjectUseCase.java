package com.configly.structure.project.application.port.in;

import com.configly.structure.project.application.port.in.command.UpdateProjectCommand;

public interface UpdateProjectUseCase {

    void handle(UpdateProjectCommand command);

}
