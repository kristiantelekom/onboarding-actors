package com.onboarding.actors.model.entity;

public enum Gender {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String genderType;

    Gender(String genderType) {
        this.genderType = genderType;
    }

    public String toString(){
        return this.genderType;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }
}
