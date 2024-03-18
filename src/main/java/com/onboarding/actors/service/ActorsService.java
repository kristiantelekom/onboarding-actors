package com.onboarding.actors.service;

import com.onboarding.actors.dao.ActorsDao;
import com.onboarding.actors.exception.ResourceNotFoundException;
import com.onboarding.actors.helper.DateConverter;
import com.onboarding.actors.helper.FilePersistenceHelper;
import com.onboarding.actors.helper.ImageConversionHelper;
import com.onboarding.actors.mapper.ObjectUtilMapper;
import com.onboarding.actors.model.entity.ActorsDTO;
import com.onboarding.actors.model.entity.ActorsEntity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

import java.util.*;

@Service
public class ActorsService {

    @Autowired
    ActorsDao actorsDao;

    @Autowired
    ObjectUtilMapper mapper;


    public ActorsDTO createActor(ActorsDTO actorsDTO) {

        if (!actorsDTO.getActorsImage().equals("")) {
            String[] imageData = ImageConversionHelper.splittedImageData(actorsDTO.getActorsImage());
            String imageExtension = ImageConversionHelper.getImageExtension(imageData[0]);
            UUID uuid = UUID.randomUUID();
            String file = "src\\main\\resources\\static\\" + uuid + "." + imageExtension;

            ImageConversionHelper.saveBase64Image(imageData[1], file);

            ActorsEntity actorEntity = mapper.map(actorsDTO, ActorsEntity.class);
            actorEntity.setActorsImageName(uuid + "." + imageExtension);
            actorsDao.createActor(actorEntity);
            ActorsDTO returnedActorsDTO = mapper.map(actorsDao.getActorById(actorEntity.getActorId()), ActorsDTO.class);
            returnedActorsDTO.setActorsImage(actorsDTO.getActorsImage());
            returnedActorsDTO.setAge(calculateActorsAge(actorsDTO));
            return mapper.map(returnedActorsDTO, ActorsDTO.class);
        }

        ActorsEntity actorEntity = mapper.map(actorsDTO, ActorsEntity.class);

        actorsDao.createActor(actorEntity);
        ActorsDTO returnedActorsDTO = mapper.map(actorsDao.getActorById(actorEntity.getActorId()), ActorsDTO.class);

        returnedActorsDTO.setAge(calculateActorsAge(actorsDTO));
        return mapper.map(returnedActorsDTO, ActorsDTO.class);

    }

    public List<ActorsDTO> getAllActors() {
        List<ActorsDTO> returnedActors = mapper.mapList(actorsDao.getActors(), ActorsDTO.class);
        for (ActorsDTO actor : returnedActors) {
            actor.setAge(calculateActorsAge(actor));
            String file = "src\\main\\resources\\static\\" + actor.getActorsImageName();
            String base64String = ImageConversionHelper.encodeImageToBase64(file);
            actor.setActorsImage(base64String);
        }
        return mapper.mapList(returnedActors, ActorsDTO.class);
    }

    public List<ActorsDTO> getActorsByName(String actorName) {
        if (Objects.equals(actorName, "")) {
            return getAllActors();
        }
        List<ActorsDTO> returnedActors = mapper.mapList(actorsDao.getActorsByName(actorName), ActorsDTO.class);
        for (ActorsDTO actor : returnedActors) {
            actor.setAge(calculateActorsAge(actor));
            String file = "src\\main\\resources\\static\\" + actor.getActorsImageName();
            String base64String = ImageConversionHelper.encodeImageToBase64(file);
            actor.setActorsImage(base64String);
        }

        return mapper.mapList(returnedActors, ActorsDTO.class);
    }


    public ActorsDTO getActorsById(Integer actorId) {
        ActorsEntity actor = actorsDao.getActorById(actorId);
        if (actor == null) {
            throw new ResourceNotFoundException("Actor with Id:" + actorId + " is not found.");
        }
        ActorsDTO returnedActorsDTO = mapper.map(actor, ActorsDTO.class);
        returnedActorsDTO.setAge(calculateActorsAge(returnedActorsDTO));
        String file = "src\\main\\resources\\static\\" + returnedActorsDTO.getActorsImageName();
        String base64String = ImageConversionHelper.encodeImageToBase64(file);
        returnedActorsDTO.setActorsImage(base64String);
        return mapper.map(returnedActorsDTO, ActorsDTO.class);
    }

    public ActorsDTO updateActor(ActorsDTO actorsDTO, Integer actorId) {
        ActorsEntity actorToUpdate = actorsDao.getActorById(actorId);
        if (actorToUpdate == null) {
            throw new ResourceNotFoundException("Actor with Id:" + actorId + " is not found.");
        }

        actorsDTO.setActorId(actorId);

        if (!Objects.equals(actorsDTO.getActorsImage(), "")) {
            if (actorToUpdate.getActorsImageName() == null) {

                String[] imageData = ImageConversionHelper.splittedImageData(actorsDTO.getActorsImage());
                String imageExtension = ImageConversionHelper.getImageExtension(imageData[0]);
                UUID uuid = UUID.randomUUID();
                String file = "src\\main\\resources\\static\\" + uuid + "." + imageExtension;
                ImageConversionHelper.saveBase64Image(imageData[1], file);
                actorsDTO.setActorsImageName(uuid + "." + imageExtension);
            } else {
                String[] imageData = ImageConversionHelper.splittedImageData(actorsDTO.getActorsImage());
                String imageExtension = ImageConversionHelper.getImageExtension(imageData[0]);

                String oldFile = actorToUpdate.getActorsImageName();
                String[] newFileSplit = oldFile.split("\\.");

                String newFile = "src\\main\\resources\\static\\" + newFileSplit[0] + "." + imageExtension;
                ImageConversionHelper.saveBase64Image(imageData[1], newFile);
                actorsDTO.setActorsImageName(newFileSplit[0] + "." + imageExtension);
            }
        }
        ActorsEntity updatedActor = actorsDao.updateActor(mapper.map(actorsDTO, ActorsEntity.class));
        ActorsDTO returnedActorsDTO = mapper.map(updatedActor, ActorsDTO.class);
        returnedActorsDTO.setActorsImage(actorsDTO.getActorsImage());
        returnedActorsDTO.setAge(calculateActorsAge(actorsDTO));

        return returnedActorsDTO;
    }

    public void deleteActor(Integer actorId) {
        ActorsEntity actorToDelete = actorsDao.getActorById(actorId);
        if (actorToDelete == null) {
            throw new ResourceNotFoundException("Actor with Id:" + actorId + " is not found.");
        }
        String file = "src\\main\\resources\\static\\" + actorToDelete.getActorsImageName();
        FilePersistenceHelper.removeFile(file);
        actorsDao.deleteActor(actorToDelete);
    }


    private Integer calculateActorsAge(ActorsDTO actorsDTO) {
        LocalDate dateOfBirth = DateConverter.convertToLocalDateViaInstant(actorsDTO.getDateOfBirth());
        LocalDate curDate = LocalDate.now();
        return Period.between(dateOfBirth, curDate).getYears();
    }


}
