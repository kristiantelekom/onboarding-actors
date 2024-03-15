package com.onboarding.actors.dao.impl;

import com.onboarding.actors.controller.ActorsController;
import com.onboarding.actors.dao.ActorsDao;
import com.onboarding.actors.model.entity.ActorsEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Transactional
public class ActorsDaoImpl implements ActorsDao {

    private EntityManager entityManager;


    public ActorsDaoImpl() {
    }

    @Autowired
    public ActorsDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public List<ActorsEntity> getActors() {
        TypedQuery<ActorsEntity> query = entityManager.createQuery("from ActorsEntity", ActorsEntity.class);
        return query.getResultList();
    }

    @Override
    public List<ActorsEntity> getActorsByName(String actorName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ActorsEntity> criteriaQuery = criteriaBuilder.createQuery(ActorsEntity.class);
        Root<ActorsEntity> root = criteriaQuery.from(ActorsEntity.class);
        criteriaQuery.select(root).where(criteriaBuilder.like(root.get("fullName"), actorName));
        List<ActorsEntity> actors = entityManager.createQuery(criteriaQuery).getResultList();
        return actors;
    }

    @Override
    public ActorsEntity getActorById(Integer actorId) {
        return entityManager.find(ActorsEntity.class, actorId);
    }

    @Override
    public ActorsEntity createActor(ActorsEntity actor) {
        try{
            entityManager.persist(actor);
            entityManager.flush();
            entityManager.close();
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return entityManager.find(ActorsEntity.class, actor.getActorId());
    }

    @Override
    public ActorsEntity updateActor(ActorsEntity actor) {
        entityManager.joinTransaction();
        entityManager.merge(actor);
        entityManager.flush();
        entityManager.clear();
        entityManager.close();
        return entityManager.find(ActorsEntity.class, actor.getActorId());
    }

    @Override
    public void deleteActor(ActorsEntity actor) {
        entityManager.remove(actor);
    }
}
