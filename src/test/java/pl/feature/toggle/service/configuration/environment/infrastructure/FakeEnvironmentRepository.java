package pl.feature.toggle.service.configuration.environment.infrastructure;

import com.ftaas.domain.environment.EnvironmentId;
import com.ftaas.domain.environment.EnvironmentName;
import com.ftaas.domain.project.ProjectId;
import pl.feature.toggle.service.configuration.environment.application.port.out.EnvironmentRepository;
import pl.feature.toggle.service.configuration.environment.domain.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeEnvironmentRepository implements EnvironmentRepository {

    private final Map<EnvironmentId, Environment> environments = new HashMap<>();

    @Override
    public EnvironmentId save(Environment environment) {
        environments.put(environment.id(), environment);
        return environment.id();
    }

    @Override
    public Optional<Environment> findById(EnvironmentId environmentId) {
        return Optional.ofNullable(environments.get(environmentId));
    }

    @Override
    public boolean existsByProjectIdAndName(ProjectId projectId, EnvironmentName environmentName) {
        return environments.values().stream()
                .anyMatch(env -> env.projectId().equals(projectId) && env.name().equals(environmentName));
    }

    @Override
    public boolean exists(EnvironmentId environmentId) {
        return environments.containsKey(environmentId);
    }

    public void clear() {
        environments.clear();
    }

    public void addNewEnvironment(Environment environment) {
        environments.put(environment.id(), environment);
    }
}
