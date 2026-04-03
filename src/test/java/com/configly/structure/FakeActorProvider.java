package com.configly.structure;


import com.configly.web.actor.Actor;
import com.configly.web.actor.ActorProvider;

public class FakeActorProvider implements ActorProvider {
    @Override
    public Actor current() {
        return Actor.system();
    }
}
