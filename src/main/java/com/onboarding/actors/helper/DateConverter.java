package com.onboarding.actors.helper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
