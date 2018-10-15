package com.youhaveamessage.validation;

import com.youhaveamessage.model.User;
import org.springframework.stereotype.Component;

@Component
public class FollowedUserValidator {

    private static final String CAN_NOT_FOLLOW_YOURSELF_MESSAGE = "You can not follow yourself";

    public ValidationErrors validate(final String userName, User userToFollow){
        final ValidationErrors validationErrors = new ValidationErrors();
        if(userName.equals(userToFollow.getUserName())){
            validationErrors.addError(CAN_NOT_FOLLOW_YOURSELF_MESSAGE);
        }
        return validationErrors;
    }
}
