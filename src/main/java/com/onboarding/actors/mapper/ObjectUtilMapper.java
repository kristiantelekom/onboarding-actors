package com.onboarding.actors.mapper;

import com.onboarding.actors.exception.InternalServerErrorException;
import com.onboarding.actors.helper.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectUtilMapper {

    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    Helper helper;

    private static final Logger logger = LogManager.getLogger(ObjectUtilMapper.class);

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        try {
            return source.stream().map(element -> modelMapper.map(element, targetClass)).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException exception occurred -> " + helper.convertObjectToString(e));
            throw new InternalServerErrorException("IllegalArgumentException exception occurred ! Error: " + e.getMessage());
        } catch (ConfigurationException e) {
            logger.error("ConfigurationException exception occurred -> " + helper.convertObjectToString(e));
            throw new InternalServerErrorException("ConfigurationException exception occurred ! Error: " + e.getMessage());
        } catch (MappingException e) {
            logger.error("MappingException exception occurred -> " + helper.convertObjectToString(e));
            throw new InternalServerErrorException("MappingException exception occurred ! Error: " + e.getMessage());
        }
    }

    public <S, T> T map(S source, Class<T> targetClass) {

        try {
            return modelMapper.map(source, targetClass);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException exception occurred -> " + helper.convertObjectToString(e));
            throw new InternalServerErrorException("IllegalArgumentException exception occurred ! Error: " + e.getMessage());
        } catch (ConfigurationException e) {
            logger.error("ConfigurationException exception occurred -> " + helper.convertObjectToString(e));
            throw new InternalServerErrorException("ConfigurationException exception occurred ! Error: " + e.getMessage());
        } catch (MappingException e) {
            logger.error("MappingException exception occurred -> " + helper.convertObjectToString(e));
            throw new InternalServerErrorException("MappingException exception occurred ! Error: " + e.getMessage());
        }
    }
}
