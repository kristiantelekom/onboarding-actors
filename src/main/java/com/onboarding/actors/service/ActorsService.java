package com.onboarding.actors.service;

import com.onboarding.actors.dao.ActorsDao;
import com.onboarding.actors.exception.ResourceNotFoundException;
import com.onboarding.actors.mapper.ObjectUtilMapper;
import com.onboarding.actors.model.entity.ActorsDTO;
import com.onboarding.actors.model.entity.ActorsEntity;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
public class ActorsService {

    @Autowired
    ActorsDao actorsDao;

    @Autowired
    ObjectUtilMapper mapper;


//    public ActorsDTO createActor(ActorsDTO actorsDTO){
//        ActorsEntity actorEntity = mapper.map(actorsDTO, ActorsEntity.class);
//        actorsDao.createActor(actorEntity);
//        ActorsDTO returnedActorsDTO = mapper.map(actorsDao.getActorById(actorEntity.getActorId()), ActorsDTO.class);
//        returnedActorsDTO.setAge(calculateActorsAge(actorsDTO));
//        return mapper.map(returnedActorsDTO, ActorsDTO.class);
//    }

    public ActorsDTO createActor(ActorsDTO actorsDTO) {


        String[] imageData = splittedImageData(actorsDTO.getActorsImage());
        String imageExtension = getImageExtension(imageData[0]);
        UUID uuid = UUID.randomUUID();
        String file = "src\\main\\resources\\static\\" + uuid + "." + imageExtension;

        saveBase64Image(imageData[1], file);

        ActorsEntity actorEntity = mapper.map(actorsDTO, ActorsEntity.class);
        actorEntity.setActorsImageName(uuid + "." + imageExtension);
        actorsDao.createActor(actorEntity);
        ActorsDTO returnedActorsDTO = mapper.map(actorsDao.getActorById(actorEntity.getActorId()), ActorsDTO.class);
        returnedActorsDTO.setActorsImage(actorsDTO.getActorsImage());
        returnedActorsDTO.setAge(calculateActorsAge(actorsDTO));
        return mapper.map(returnedActorsDTO, ActorsDTO.class);

    }





    public List<ActorsDTO> getAllActors(){
        List<ActorsDTO> returnedActors = mapper.mapList(actorsDao.getActors(), ActorsDTO.class);
        for (ActorsDTO actor: returnedActors) {
            actor.setAge(calculateActorsAge(actor));
            String file = "src\\main\\resources\\static\\"+actor.getActorsImageName();
            String base64String = encodeImageToBase64(file);
            actor.setActorsImage(base64String);
        }
        return mapper.mapList(returnedActors, ActorsDTO.class);
    }

    public List<ActorsDTO> getActorsByName(String actorName){
        if(Objects.equals(actorName, "")){
            return getAllActors();
        }
        List<ActorsDTO> returnedActors = mapper.mapList(actorsDao.getActorsByName(actorName), ActorsDTO.class);
        for (ActorsDTO actor: returnedActors) {
            actor.setAge(calculateActorsAge(actor));
            String file = "src\\main\\resources\\static\\"+actor.getActorsImageName();
            String base64String = encodeImageToBase64(file);
            actor.setActorsImage(base64String);
        }

        return mapper.mapList(returnedActors, ActorsDTO.class);
    }



    public ActorsDTO getActorsById(Integer actorId){
        ActorsEntity actor = actorsDao.getActorById(actorId);
        if(actor == null){
            throw new ResourceNotFoundException("Actor with Id:" + actorId + " is not found.");
        }
        ActorsDTO returnedActorsDTO = mapper.map(actor, ActorsDTO.class);
        returnedActorsDTO.setAge(calculateActorsAge(returnedActorsDTO));
        String file = "src\\main\\resources\\static\\"+returnedActorsDTO.getActorsImageName();
        String base64String = encodeImageToBase64(file);
        returnedActorsDTO.setActorsImage(base64String);
        return mapper.map(returnedActorsDTO, ActorsDTO.class);
    }

    public ActorsDTO updateActor(ActorsDTO actorsDTO, Integer actorId){
        ActorsEntity actorToUpdate = actorsDao.getActorById(actorId);
        if(actorToUpdate == null){
            throw new ResourceNotFoundException("Actor with Id:" + actorId + " is not found.");
        }

        actorsDTO.setActorId(actorId);


        if(!Objects.equals(actorsDTO.getActorsImage(), "")){
            String[] imageData = splittedImageData(actorsDTO.getActorsImage());
            String imageExtension = getImageExtension(imageData[0]);

            String oldFile = actorToUpdate.getActorsImageName();
            String[] newFileSplit = oldFile.split("\\.");

            String newFile = "src\\main\\resources\\static\\" + newFileSplit[0] + "." +imageExtension;
            saveBase64Image(imageData[1], newFile);
            actorsDTO.setActorsImageName(newFileSplit[0] + "." +imageExtension);
//            actorsDTO.setActorsImage(newFileSplit[0] + "." +imageExtension);
        }


        ActorsEntity updatedActor = actorsDao.updateActor(mapper.map(actorsDTO, ActorsEntity.class));
        ActorsDTO returnedActorsDTO = mapper.map(updatedActor, ActorsDTO.class);
        returnedActorsDTO.setActorsImage(actorsDTO.getActorsImage());
        returnedActorsDTO.setAge(calculateActorsAge(actorsDTO));

        return returnedActorsDTO;
    }

    public void deleteActor(Integer actorId){
        ActorsEntity actorToDelete = actorsDao.getActorById(actorId);
        if(actorToDelete == null){
            throw new ResourceNotFoundException("Actor with Id:" + actorId + " is not found.");
        }
        String file = "src\\main\\resources\\static\\"+actorToDelete.getActorsImageName();
        removeFile(file);
        actorsDao.deleteActor(actorToDelete);
    }

    private void removeFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) { // Check if the file exists
            if (file.delete()) { // Attempt to delete the file
                System.out.println("File deleted successfully: " + filePath);
            } else {
                System.err.println("Failed to delete the file: " + filePath);
            }
        } else {
            System.err.println("File does not exist: " + filePath);
        }
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private Integer calculateActorsAge(ActorsDTO actorsDTO) {
        LocalDate dateOfBirth = convertToLocalDateViaInstant(actorsDTO.getDateOfBirth());
        LocalDate curDate = LocalDate.now();
        return Period.between(dateOfBirth, curDate).getYears();
    }

    private String getImageExtension(String imageMetadata){
        //data:image/jpeg;base64
        String[] firstSplit = imageMetadata.split("/");
        String[] extension = firstSplit[1].split(";");
        return extension[0];
    }

    private String[] splittedImageData(String base64Data){
        String[] decoded = base64Data.split(",");
        System.out.println(decoded[0]);
        System.out.println(decoded[1]);
        return decoded;
    }

    private void saveBase64Image(String base64Data, String fileName) {
        try {

            // Decode Base64 data
            byte[] decodedData = Base64.getDecoder().decode(base64Data);
            //byte[] decodedData = Base64.getMimeDecoder().decode(decoded[1]);

            // Write the decoded data to a file
            Files.write(Paths.get(fileName), decodedData);

            System.out.println("Image '" + fileName + "' saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    private String encodeImageToBase64(String filePath) {
        String base64String = "";
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();

            // Encode bytes to Base64
            base64String = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64String;
    }
}
