package com.onboarding.actors.dao;

import com.onboarding.actors.model.entity.ActorsEntity;

import java.util.List;

public interface ActorsDao {

    List<ActorsEntity> getActors();
    List<ActorsEntity> getActorsByName(String actorName);
    ActorsEntity getActorById(Integer actorId);
    ActorsEntity createActor(ActorsEntity actor);
    ActorsEntity updateActor(ActorsEntity actor);
    void deleteActor(ActorsEntity actor);

}
