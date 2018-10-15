# you-have-a-message
Application which allows user to publish nessages and follow other users.

## Requests
### POST /users/{userName}/messages - creates message
Request body:
```
{
  "message": "your message"
}
```
Returns HTTP 200 OK when success

Returns HTTP 400 BAD_REQUEST with validation errors:
```
{
    "errorMessages": [
        "Message is to long. Maximum number of characters is 140."
    ]
}
```
### GET /users/{userName}/messages - returns list of user messages
Example:
```
[
    {
        "instant": "2018-10-15T18:56:36.524Z",
        "message": "my second message"
    },
    {
        "instant": "2018-10-15T18:45:19.696Z",
        "message": "my first message"
    }
]
```
### POST /users/{userName}/followed - adds user to follow
Request body specifies user to follow. Request body example:
```
{
    "userName": "rob"
}
```
Returns HTTP 200 OK when success

Returns HTTP 400 BAD_REQUEST with validation errors:
```
{
    "errorMessages": [
        "You can not follow yourself"
    ]
}
```
### DELETE /users/{userName}/followed - removes user from followed
Request body specifies user to stop following. Request body example:
```
{
    "userName": "rob"
}
```
Returns HTTP 200 OK when success.
### GET /users/{userName}/timeline - returns list followed users messages starting from latest together with name of user who published message
```
[
    {
        "userName": "judy",
        "message": {
            "instant": "2018-10-15T20:06:04.018Z",
            "message": "seventhMessage"
        }
    },
    {
        "userName": "rob",
        "message": {
            "instant": "2018-10-15T20:05:51.473Z",
            "message": "sixthMessage"
        }
    }
]
```
