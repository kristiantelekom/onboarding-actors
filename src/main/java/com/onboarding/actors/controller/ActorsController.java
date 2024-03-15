package com.onboarding.actors.controller;

import com.onboarding.actors.model.entity.ActorsDTO;
import com.onboarding.actors.service.ActorsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.PreparedStatement;
import java.util.List;

@RestController
@RequestMapping("actors")
public class ActorsController {

    @Autowired
    ActorsService actorsService;

    @GetMapping
    public ResponseEntity<List<ActorsDTO>> getActorsByName(@RequestParam(value = "fullName", required = false, defaultValue = "") String actorName){
        return new ResponseEntity<>(actorsService.getActorsByName(actorName), HttpStatus.OK);
    }

    @GetMapping("/{actorId:[0-9]+}")
    public ResponseEntity<ActorsDTO> getActorById(@PathVariable Integer actorId){
        return  new ResponseEntity<>(actorsService.getActorsById(actorId), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<ActorsDTO> createActor(@Valid @RequestBody ActorsDTO actorsDto){
        ActorsDTO createdActor = actorsService.createActor(actorsDto);
        return new ResponseEntity<>(createdActor, HttpStatus.CREATED);
    }




    @PutMapping("/{actorId:[0-9]+}")
    public ResponseEntity<ActorsDTO> updateActor(@Valid @RequestBody ActorsDTO actorsDto, @PathVariable Integer actorId){
        ActorsDTO updatedActor = actorsService.updateActor(actorsDto, actorId);
        return new ResponseEntity<>(updatedActor, HttpStatus.OK);
    }

    @DeleteMapping("/{actorId:[0-9]+}")
    public ResponseEntity<Void> deleteActor(@PathVariable Integer actorId){
        actorsService.deleteActor(actorId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
