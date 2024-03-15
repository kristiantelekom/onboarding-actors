package com.onboarding.actors.helper;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

@Component
public class Helper {

    public String convertObjectToString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

}
