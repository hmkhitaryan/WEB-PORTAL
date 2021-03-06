package com.egs.account.validator;

import com.egs.account.model.FileBucket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class FileValidator implements Validator {

    public boolean supports(Class<?> clazz) {
        return FileBucket.class.isAssignableFrom(clazz);
    }

    public void validate(Object obj, Errors errors) {
        FileBucket file = (FileBucket) obj;
        long fileSize = file.getFile().getSize();
        if (file.getFile() != null) {
            if (fileSize == 0) {
                errors.rejectValue("file", "missing.file");
            }
            if (fileSize >  2000000) {
                errors.rejectValue("file", "tooBig.file");
            }
        }
    }
}