import pytest
from app import app as test_app, data_creation_for_testing, allowed_special
import os


@pytest.fixture()
def app():
    test_app.config.update({"TESTING": True,
                            "DEBUG": True})

    data_creation_for_testing()

    yield test_app


def client(app):
    return app.test_client()


def runner(app):
    return app.test_cli_runner()


known_tokens = {}


def test_login_with_username(app):
    with app.app_context():
        test_client = client(app)
        test_json = {"useremail": "john_doe",
                     "password": "secret"}
        response = test_client.post("/login", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "auth-token" in response.json
        assert response.json["success"]
    known_tokens[test_json["useremail"]] = response.json["auth-token"]


def test_login_with_email(app):
    with app.app_context():
        test_client = client(app)
        test_json = {"useremail": "johndoe@email.com",
                     "password": "secret"}
        response = test_client.post("/login", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "auth-token" in response.json
        assert response.json["success"]
        known_tokens[test_json["useremail"]] = response.json["auth-token"]


def test_login_incorrect_credentials(app):
    with app.app_context():
        test_client = client(app)
        test_jsons = [
            {"useremail": "john_doe",
             "password": "wrongsecret"},
            {"useremail": "johndoe@email.com",
             "password": "wrongsecret"},
            {"useremail": "notauser",
             "password": "secret"},
            {"useremail": "notauser",
             "password": "wrongsecret"},
        ]
        for test_json in test_jsons:
            response = test_client.post("/login", json=test_json)
            assert response.status_code == 200
            assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
            assert response.json["success"] == False
            assert response.json["message"] == "Username/Email or password is incorrect"


def test_get_user(app):
    with app.app_context():
        test_client = app.test_client()
        token = known_tokens["john_doe"]
        response = test_client.get("/user", headers={"auth-token": token})
        assert "user" in response.json and "email" in response.json
        assert response.json["user"] == "john_doe"
        assert response.json["email"] == "johndoe@email.com"

def test_get_user_no_auth(app):
    with app.app_context():
        test_client = app.test_client()
        response = test_client.get("/user")
        assert response.status_code == 401
        assert "success" in response.json and "message" in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Invalid token"

def test_signup_success(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "jane_doe",
            "email": "janedoe@email.com",
            "password": "1234567!"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "auth-token" in response.json
        assert response.json["success"]
        known_tokens[test_json["user"]] = response.json["auth-token"]
        known_tokens[test_json["email"]] = response.json["auth-token"]


def test_signup_username_too_short(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "jo",
            "email": "janedoe@email.com",
            "password": "1234567!"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Username must be between 3 and 20 characters long"


def test_signup_username_too_long(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "johndoejohndoejohndoejohndoe",
            "email": "janedoe@email.com",
            "password": "1234567!"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Username must be between 3 and 20 characters long"


def test_signup_taken_username(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "john_doe",
            "email": "janedoe@email.com",
            "password": "1234567!"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Username already taken"


def test_signup_invalid_characters_in_username(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "jane_doe!%';",
            "email": "johndoe@email.com",
            "password": "1234567!"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Username must only contain alphanumeric characters and _"


def test_signup_taken_email(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "jane_doe",
            "email": "johndoe@email.com",
            "password": "1234567!"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Email already taken"


def test_signup_invalid_email(app):
    with app.app_context():
        test_client = app.test_client()
        test_json = {
            "user": "jane_doe",
            "password": "1234567!"
        }
        invalid_emails = [
            "thisisnotanemail.com",
            "thisisjusttext",
            "@.com",
            "wow@email"
        ]
        for email in invalid_emails:
            test_json["email"] = email
            response = test_client.post("/signup", json=test_json)
            assert response.status_code == 200
            assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
            assert response.json["success"] == False
            assert response.json["message"] == "Invalid email format"


def test_signup_taken_password_too_short(app):
    with (app.app_context()):
        test_client = app.test_client()
        test_json = {
            "user": "jane_doe",
            "email": "johndoe@email.com",
            "password": "1234567"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Password must be at least 8 characters long"


def test_signup_invalid_password_no_special(app):
    with (app.app_context()):
        test_client = app.test_client()
        test_json = {
            "user": "jane_doe",
            "email": "johndoe@email.com",
            "password": "123456789"
        }
        response = test_client.post("/signup", json=test_json)
        assert response.status_code == 200
        assert "success" in response.json and "message" in response.json and "auth-token" not in response.json
        assert response.json["success"] == False
        assert response.json["message"] == \
               f"Password must have alphanumeric characters and at least one special character: {allowed_special}"


def test_refresh_token(app):
    with (app.app_context()):
        test_client = app.test_client()
        response = test_client.post("/refresh_token", headers={"auth-token": known_tokens["john_doe"]})
        assert response.status_code == 200
        assert "success" in response.json
        assert response.json["success"]


def test_refresh_token_no_auth(app):
    with (app.app_context()):
        test_client = app.test_client()
        response = test_client.post("/refresh_token")
        assert response.status_code == 401
        assert "success" in response.json and "message" in response.json
        assert response.json["success"] == False
        assert response.json["message"] == "Invalid token"
