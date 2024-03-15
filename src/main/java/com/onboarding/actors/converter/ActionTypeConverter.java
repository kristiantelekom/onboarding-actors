package com.onboarding.actors.converter;

import com.onboarding.actors.model.entity.Gender;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class ActionTypeConverter implements AttributeConverter<Gender, String> {
    @Override
    public String convertToDatabaseColumn(Gender gender) {
        if (gender == null) {
            return null;
        }
        return gender.toString();
    }

    @Override
    public Gender convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }

        return Stream.of(Gender.values())
                .filter(gender -> gender.getGenderType().equals(s))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }



}
