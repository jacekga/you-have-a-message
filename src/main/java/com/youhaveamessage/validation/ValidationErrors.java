package com.youhaveamessage.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrors {

    private final List<String> errorMessages = new ArrayList<>();

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public boolean hasErrors(){
        return !errorMessages.isEmpty();
    }

    public boolean addError(final String errorMessage){
        return errorMessages.add(errorMessage);
    }
}
