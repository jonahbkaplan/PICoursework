# Purpose

The **AuthService** allows user to register and login to the app to obtain an **authtoken**.
This **authtoken** is used to authenticate the user when making requests to the other services.
Such as requesting metric data from the MongoDB database or making changes to userdata.
This allows the user to access and modify their own data while protecting it from other users.

The **authtoken** will expire an hour after it was last refrshed.
This is to ensure tokens that are not being used are removed.
A token will also expire if the user logs out.
Therefore, while a token is in use, they should be refreshed regularly.

See */refresh_token* below for more information on how to refresh the **authtoken**.

Below, please find the different endpoints that can be accessed and the expected request and response formats for usage in the frontend.

# Usage 

To use the **AuthService** in the frontend, the following steps are required:
- Install all python dependencies with `pip install -r requirements.txt`
- Run the **AuthService** with `python app.py` to access locally. The base URL will be `http://localhost:5000`

## Remote Access
- To access remotely, run `gunicorn --config gunicorn_config.py app:app` in a webserver. The base URL will be `http://<server_ip>/`

## MongoDB Connection

Within MongoDB, a user database is created which contains a collection of users. 

### The following is stored against each user:

- **user_id**: A unique identifier for each user
- **user**: The user's username
- **email**: The user's email
- **pass_hash**: The user's hashed password
- **salt** : The salt used to hash the password

# Endpoints

## POST /signup

### Request Body

```json
{
  "user": "John_Doe",
  "email": "johndoe@mail.com",
  "password": "123456"
}
```
### Response
    
**If a valid username, email and password are provided, the response will be:**
```json
{
"success": true,
"authtoken": "f993acba-9c6d-48fe-8aa9-49d1ec8d56cc"
}
```

**An invalid response will occur for several reasons:**
- If a username or email is already in use
- A username is not between 3 and 20 characters long
- If a username contains special characters besides _
- If the email is not in the correct format
- A password is less than 8 characters long or does not include a special character
- If a field is missing/blank

**E.g. A message will be returned indicating what was wrong with the request**
```json
{
"success": false,
"message": "Username/Email already taken"
}
```

## POST /login

### Request Body
```json
{
  "useremail": "johndoe@gmail.com",
  "password": "123456"
}
```
**A request to login will accept either a username or email.**
These have been combined into a single field for ease of use.
### Response
```json
{
  "success": true,
  "authtoken": "f993acba-9c6d-48fe-8aa9-49d1ec8d56cc"
}
```
**An invalid response will occur for several reasons:**
- If the username/email does not exist
- If the password is incorrect
- If a field is missing/blank
```json
{
  "success": false,
  "message": "Username/Email or password is incorrect"
}
```

# Restricted Endpoints

These endpoints require a valid **authtoken** to be provided in the **request header** to access them. 

**authtokens** can be obtained from */login* and */signup*

**E.g.**

```json
{
  "authtoken": "f993acba-9c6d-48fe-8aa9-49d1ec8d56cc"
}
```

## POST /refresh_token

The **authtoken** will expire after an hour if it is not regularly refreshed.
The token can be refreshed simply by sending the following request:

**Requires an **authtoken** to be provided in the request header.**

### Response

```json
{
  "success": true
}
```

An invalid response will occur if the **authtoken** is not provided or is incorrect.
```json
{
  "success": false,
  "message": "Invalid token"
}
```

Please note that the **authtoken** does not change when refreshed and so only 'success' is returned.

## POST /logout

Logout will remove the **authtoken** from the database and the user will need to login again to obtain a new one.
This endpoint will always respond successfully.

**Requires an **authtoken** to be provided in the request header.**

### Response

```json
{
  "success": true
}
```

## GET /user

This endpoint will return the user's username, email and user_id. 
This endpoint is useful for verifying the user's **authtoken**.

**Requires an **authtoken** to be provided in the request header.**

### Response

```json
{
  "user": "John_Doe",
  "email": "johndoe@mail.com"
}
```

## POST /delete_user

If you need to delete a user (which is probably quite unlikely for now). 

**Requires an **authtoken** to be provided in the request header.**

### Response

```json
{
  "success": true
}
```
