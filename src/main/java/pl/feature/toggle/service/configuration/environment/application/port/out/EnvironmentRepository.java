package pl.feature.toggle.service.configuration.environment.application.port.out;

import com.ftaas.domain.environment.EnvironmentName;
import com.ftaas.domain.project.ProjectId;
import pl.feature.toggle.service.configuration.environment.domain.Environment;
import com.ftaas.domain.environment.EnvironmentId;

import java.util.Optional;

public interface EnvironmentRepository {

    EnvironmentId save(Environment environment);

    Optional<Environment> findById(EnvironmentId environmentId);

    boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName);

    boolean exists(EnvironmentId environmentId);
}
