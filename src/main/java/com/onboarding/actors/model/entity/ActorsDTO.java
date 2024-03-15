package com.onboarding.actors.model.entity;

import java.util.Date;

public class ActorsDTO {

    private Integer actorId;

    private String fullName;

    private String gender;

    private String jobTitle;

    private Date dateOfBirth;

    private String placeOfBirth;
    private String children;
    private String spouse;
    private String parents;
    private String biography;

    private String actorsImage;
    private String actorsImageName;
    private Integer age;

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    public String getParents() {
        return parents;
    }

    public void setParents(String parents) {
        this.parents = parents;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getActorsImage() {
        return actorsImage;
    }

    public void setActorsImage(String actorsImage) {
        this.actorsImage = actorsImage;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getActorsImageName() {
        return actorsImageName;
    }

    public void setActorsImageName(String actorsImageName) {
        this.actorsImageName = actorsImageName;
    }
}
