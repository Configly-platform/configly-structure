package pl.feature.toggle.service.configuration.project.domain;

public enum ProjectStatus {
    ACTIVE,
    ARCHIVED;

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean isArchived() {
        return this == ARCHIVED;
    }
}
