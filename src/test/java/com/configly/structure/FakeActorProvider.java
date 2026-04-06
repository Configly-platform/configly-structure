package com.configly.structure;


import com.configly.web.model.actor.Actor;
import com.configly.web.model.actor.ActorProvider;

public class FakeActorProvider implements ActorProvider {
    @Override
    public Actor current() {
        return Actor.system();
    }
}
