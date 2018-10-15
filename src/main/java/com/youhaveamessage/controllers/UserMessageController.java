package com.youhaveamessage.controllers;

import com.youhaveamessage.model.Message;
import com.youhaveamessage.model.User;
import com.youhaveamessage.services.UserMessageService;
import com.youhaveamessage.validation.FollowedUserValidator;
import com.youhaveamessage.validation.MessageSizeValidator;
import com.youhaveamessage.validation.ValidationErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserMessageController {

    private final UserMessageService service;
    private final MessageSizeValidator messageSizeValidator;
    private final FollowedUserValidator followedUserValidator;

    @Autowired
    public UserMessageController(final UserMessageService service,
                                 final MessageSizeValidator messageSizeValidator,
                                 final FollowedUserValidator followedUserValidator){
        this.service = service;
        this.messageSizeValidator = messageSizeValidator;
        this.followedUserValidator = followedUserValidator;
    }

    @PostMapping("/{userName}/messages")
    public ResponseEntity addMessage(@PathVariable String userName, @RequestBody Message message){
        final ValidationErrors validationErrors = messageSizeValidator.validate(message);
        if(validationErrors.hasErrors()){
            return ResponseEntity.badRequest().body(validationErrors);
        }
        if(service.addUserMessage(userName, message)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/{userName}/messages")
    public ResponseEntity getUserMessages(@PathVariable String userName){
        final List<Message> userMessages = service.getMessagesFromLatest(userName);
        if(userMessages.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMessages);
    }

    @PostMapping("/{userName}/followed")
    public ResponseEntity startFollowingUser(@PathVariable String userName, @RequestBody User userToFollow){
        final ValidationErrors validationErrors = followedUserValidator.validate(userName, userToFollow);
        if(validationErrors.hasErrors()){
            return ResponseEntity.badRequest().body(validationErrors);
        }
        if(service.startFollowing(userName, userToFollow.getUserName())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userName}/followed")
    public ResponseEntity stopFollowingUser(@PathVariable String userName, @RequestBody User userToStopFollow){
        if(service.stopFollowing(userName, userToStopFollow.getUserName())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{userName}/timeline")
    public ResponseEntity getUserTimeline(@PathVariable String userName){
        if(service.userExists(userName)){
            return ResponseEntity.ok(service.getTimeline(userName));
        }
        return ResponseEntity.notFound().build();
    }
}
