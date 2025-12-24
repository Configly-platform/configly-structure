package pl.feature.toggle.service.configuration;

import pl.feature.toggle.service.model.security.Actor;
import pl.feature.toggle.service.model.security.ActorProvider;

public class FakeActorProvider implements ActorProvider {
    @Override
    public Actor current() {
        return Actor.system();
    }
}
