package pl.feature.toggle.service.configuration;


import pl.feature.toggle.service.model.security.actor.Actor;
import pl.feature.toggle.service.model.security.actor.ActorProvider;

public class FakeActorProvider implements ActorProvider {
    @Override
    public Actor current() {
        return Actor.system();
    }
}
