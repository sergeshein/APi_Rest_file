package com.example.APi_Rest_file.util;


import dto.Request;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.File;

@Component
public class RequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Request.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Request request = (Request) target;

        if (request.getFilePath() == null)
            errors.rejectValue("filePath", "File path is null!");

        String filePath = request.getFilePath();

        File file = new File(filePath);

        if (!file.exists())
            errors.rejectValue("filePath", "File with path " + filePath + " doesn't exists!");

    }
}

