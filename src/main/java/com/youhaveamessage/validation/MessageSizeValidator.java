package com.youhaveamessage.validation;

import com.youhaveamessage.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageSizeValidator {

    private static final String TOO_MANY_CHARACTERS_MESSAGE =
            "Message is to long. Maximum number of characters is 140.";

    public ValidationErrors validate(final Message message){
        final ValidationErrors validationErrors = new ValidationErrors();
        if(message.getMessage().length() > 140){
            validationErrors.addError(TOO_MANY_CHARACTERS_MESSAGE);
        }
        return validationErrors;
    }
}
