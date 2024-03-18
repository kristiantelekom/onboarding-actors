package com.onboarding.actors.controller;

import com.onboarding.actors.model.entity.ActorsDTO;
import com.onboarding.actors.service.ActorsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("actors")
public class ActorsController {

    @Autowired
    ActorsService actorsService;

    @GetMapping
    public ResponseEntity<List<ActorsDTO>> getActorsByName(@RequestParam(value = "fullName", required = false, defaultValue = "") String actorName){

        List<ActorsDTO> actorsDTOS = actorsService.getActorsByName(actorName);

        for (ActorsDTO actor: actorsDTOS
             ) {
            actor.setHref(WebMvcLinkBuilder.linkTo(methodOn(ActorsController.class).getActorById(actor.getActorId())).toUriComponentsBuilder().toUriString());
        }

        return new ResponseEntity<>(actorsDTOS, HttpStatus.OK);
    }



    @GetMapping("/{actorId:[0-9]+}")
    public ResponseEntity<ActorsDTO> getActorById(@PathVariable Integer actorId){
        ActorsDTO actorsDTO =  actorsService.getActorsById(actorId);
        actorsDTO.setHref(WebMvcLinkBuilder.linkTo(methodOn(ActorsController.class).getActorById(actorId)).toUriComponentsBuilder().toUriString());
        return new ResponseEntity<>(actorsDTO, HttpStatus.OK);
    }




    @PostMapping()
    public ResponseEntity<ActorsDTO> createActor(@Valid @RequestBody ActorsDTO actorsDto){
        ActorsDTO createdActor = actorsService.createActor(actorsDto);
        createdActor.setHref(WebMvcLinkBuilder.linkTo(methodOn(ActorsController.class).getActorById(createdActor.getActorId())).toUriComponentsBuilder().toUriString());
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }




    @PutMapping("/{actorId:[0-9]+}")
    public ResponseEntity<ActorsDTO> updateActor(@Valid @RequestBody ActorsDTO actorsDto, @PathVariable Integer actorId){
        ActorsDTO updatedActor = actorsService.updateActor(actorsDto, actorId);
        updatedActor.setHref(WebMvcLinkBuilder.linkTo(methodOn(ActorsController.class).getActorById(updatedActor.getActorId())).toUriComponentsBuilder().toUriString());
        return new ResponseEntity<>(updatedActor, HttpStatus.OK);
    }

    @DeleteMapping("/{actorId:[0-9]+}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer actorId){
        actorsService.deleteActor(actorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
