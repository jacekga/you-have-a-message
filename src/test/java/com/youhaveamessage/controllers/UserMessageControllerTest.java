package com.youhaveamessage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.youhaveamessage.YouHaveAMessageApplication;
import com.youhaveamessage.validation.ValidationErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {YouHaveAMessageApplication.class})
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserMessageControllerTest {

    @Autowired
    private MockMvc mvc;
    private final String myUserName = "myUser";
    private ObjectWriter errorWriter = new ObjectMapper().writerFor(ValidationErrors.class);


    @Test
    void testRequestToAddUserShouldRespondWithOkWhenMessageLengthCorrect() throws Exception {
        String messageText = "FirstMessage";

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createMessageJson(messageText)))
                .andExpect(status().isOk());
    }

    @Test
    void testRequestToAddUserShouldRespondWithBadRequestWhenMessageTooLong() throws Exception {
        ValidationErrors errors = new ValidationErrors();
        errors.addError("Message is to long. Maximum number of characters is 140.");

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createTooLongMessage()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorWriter.writeValueAsString(errors)));
    }

    @Test
    void testRequestShouldReturnListOfUserMessages() throws Exception {
        String messageText = "FirstMessage";

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createMessageJson(messageText)))
                .andExpect(status().isOk());

        mvc.perform(get(messagesUrl(myUserName)))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"message\": \"" + messageText + "\"}]", false));
    }

    @Test
    void testRequestShouldReturnNotFoundWhenNoMessagesPostedByUser() throws Exception {
        mvc.perform(get(messagesUrl(myUserName)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRequestToAddFollowedUserShouldReturnOkWhenSuccess() throws Exception {
        String messageText = "FirstMessage";
        String userToFollow = "userToFollow";

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createMessageJson(messageText)))
                .andExpect(status().isOk());

        mvc.perform(post(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(userToFollow)))
                .andExpect(status().isOk());
    }

    @Test
    void testRequestShouldReturnBadRequestWhenUserTriesToFollowItself() throws Exception {
        String messageText = "FirstMessage";
        ValidationErrors errors = new ValidationErrors();
        errors.addError("You can not follow yourself");

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createMessageJson(messageText)))
                .andExpect(status().isOk());

        mvc.perform(post(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(myUserName)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorWriter.writeValueAsString(errors)));
    }

    @Test
    void testRequestShouldReturnNotFouldWhenUserDoesNotExists() throws Exception {
        String userToFollow = "userToFollow";

        mvc.perform(post(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(userToFollow)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRequestToDeleteUserFromFollowedShouldReturnOkWhenSuccess() throws Exception {
        String messageText = "FirstMessage";
        String userToFollow = "userToFollow";

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createMessageJson(messageText)))
                .andExpect(status().isOk());

        mvc.perform(post(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(userToFollow)))
                .andExpect(status().isOk());

        mvc.perform(delete(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(userToFollow)))
                .andExpect(status().isOk());
    }

    @Test
    void testRequestDoDeleteFollowedShouldReturnNotFoundWhenApplicationUserNotExists() throws Exception {
        String userToFollow = "userToFollow";

        mvc.perform(delete(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(userToFollow)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRequestToDeleteFollowedUserShouldReturNotFoundWhenUserNotInFollowedList() throws Exception {
        String messageText = "FirstMessage";
        String userToFollow = "userToFollow";

        mvc.perform(post(messagesUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createMessageJson(messageText)))
                .andExpect(status().isOk());

        mvc.perform(delete(followedUserUrl(myUserName))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(createUserJson(userToFollow)))
                .andExpect(status().isNotFound());
    }

    private String createMessageJson(String message){
        return "{"
                + "\"message\": \"" + message + "\""
                + "}";
    }

    private String createTooLongMessage(){
        StringBuilder message = new StringBuilder("{ \"message\": \"");
        for(int i = 0;  i <= 140; i++){
            message.append("i");
        }
        message.append("\"}");
        return message.toString();
    }

    private String createUserJson(String userToFollow){
        return "{"
                + "\"userName\": \"" + userToFollow + "\""
                + "}";
    }

    private String messagesUrl(String userName){
        return "/users/" + userName + "/messages";
    }

    private String followedUserUrl(String userName){
        return "/users/" + userName + "/followed";
    }
}
