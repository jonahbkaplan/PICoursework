from flask import Flask, request, jsonify
import datetime
from flask_cors import CORS
import bcrypt
import uuid
import string
from email.utils import parseaddr
import pymongo
import mongomock
import traceback
import json

auth_config = json.load(open("auth_config.json"))

app = Flask("AuthService")
CORS(app, resources={r"/*": {"origins": "*"}})

def data_creation_for_testing():
    global mongo_client
    global users
    mongo_client = mongomock.MongoClient()
    users = mongo_client.user_db.users
    salt = bcrypt.gensalt()
    users.insert_one({"user_id": "1234",
                      "user": "john_doe",
                      "pass_hash": bcrypt.hashpw("secret".encode(), salt).decode("ascii"),
                      "salt": salt.decode("ascii"),
                      "email": "johndoe@email.com"})


mongo_client = pymongo.MongoClient(auth_config["mongo_uri"])
users = mongo_client.user_db.users

if auth_config["add_admin"]:
    salt = bcrypt.gensalt()
    users.insert_one({"user_id": "1",
                      "user": "admin",
                      "pass_hash": bcrypt.hashpw("admin".encode(), salt).decode("ascii"),
                      "salt": salt.decode("ascii"),
                      "email": "admin@gmail.com"})

alphanum = string.ascii_letters + string.digits
allowed_special = "!@#$%^&*_+?"

tokens = {}

def create_token(user_id):
    # Create a new token that will expire in 1 hour
    token = str(uuid.uuid4())
    expiry = datetime.datetime.now() + datetime.timedelta(hours=1)
    tokens[token] = (user_id, expiry)
    return token


def get_user_from_token():
    token = request.headers.get("auth-token")

    # Check if the token is valid
    if token not in tokens:
        return None, None

    # Check if the token has expired
    if datetime.datetime.now() > tokens[token][1]:
        del tokens[token]
        return None, None

    # Get the user_id associated with the token
    user_id = tokens[token][0]
    return users.find_one({"user_id": user_id}), token


@app.route('/refresh_token', methods=["POST", "OPTIONS"])
def refresh_token():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}

    # Try and get the user_id associated with the token
    user_id, token = get_user_from_token()

    # Check if the token was valid
    if not token:
        return {"success": False, "message": "Invalid token"}, 401

    expiry = datetime.datetime.now() + datetime.timedelta(hours=1)
    tokens[token] = (user_id, expiry)
    return {"success": True}


@app.route('/login', methods=["POST", "OPTIONS"])
def login():
    try:
        if request.method == "OPTIONS":
            return {"Access-Control-Allow-Origin": "*"}

        data = request.get_json()

        if data["useremail"] == "" or data["password"] == "":
            raise KeyError()

        # Check if the user exists
        user = users.find_one({"user": data["useremail"]}) or users.find_one({"email": data["useremail"]})

        if not user:
            return {"success": False, "message": "Username/Email or password is incorrect"}

        pass_hash = bcrypt.hashpw(data["password"].encode(), user["salt"].encode()).decode("ascii")

        # Check if the password is correct

        if pass_hash == user["pass_hash"]:
            # Create token and send a successful login response
            token = create_token(user["user_id"])

            return {"success": True, "auth-token": token}

        return {"success": False, "message": "Username/Email or password is incorrect"}
    except KeyError:
        return {"success": False, "message": "Missing required fields (useremail, password)"}, 400
    except Exception as e:
        print(traceback.format_exc())
        return {"success": False, "message": str(e)}, 500


@app.route('/logout', methods=["POST", "OPTIONS"])
def logout():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}

    # Get the user associated with the token
    user, token = get_user_from_token()

    # Log out the user from this session by deleting the token
    if token in tokens:
        del tokens[token]
    return {"success": True}


@app.route('/signup', methods=["POST", "OPTIONS"])
def signup():
    try:
        if request.method == "OPTIONS":
            return {"Access-Control-Allow-Origin": "*"}
        data = request.get_json()

        if data["user"] == "" or data["email"] == "" or data["password"] == "":
            raise KeyError()

        if any(char not in alphanum + "_" for char in data["user"]):
            return {"success": False, "message": "Username must only contain alphanumeric characters and _"}

        if len(data["user"]) < 3 or len(data["user"]) > 20:
            return {"success": False, "message": "Username must be between 3 and 20 characters long"}

        try:
            email_username, email_temp = data["email"].split("@")
            mail_server, domain = email_temp.split(".")
            if email_username == "" or mail_server == "" or domain == "":
                raise Exception
        except:
            return {"success": False, "message": "Invalid email format"}

        if len(data["password"]) < 8:
            return {"success": False, "message": "Password must be at least 8 characters long"}
        if any(char not in alphanum + "!@#$%^&*_+?" for char in data["password"]) or not any(
                char in allowed_special for char in data["password"]):
            return {"success": False,
                         "message": f"Password must have alphanumeric characters and at least one special character: {allowed_special}"}

        # Check if the username is already taken
        if users.find_one({"user": data["user"]}):
            return {"success": False, "message": "Username already taken"}
        if users.find_one({"email": data["email"]}):
            return {"success": False, "message": "Email already taken"}

        # Hash the password
        salt = bcrypt.gensalt()
        pass_hash = bcrypt.hashpw(data["password"].encode(), salt).decode("ascii")
        salt = salt.decode("ascii")

        # Create a new user

        new_user = {"user_id": str(uuid.uuid4()),
                    "user": data["user"],
                    "pass_hash": pass_hash,
                    "salt": salt,
                    "email": data["email"]}

        users.insert_one(new_user)

        token = create_token(new_user["user_id"])

        return {"success": True, "auth-token": token}
    except KeyError:
        print(traceback.format_exc())
        return {"success": False, "message": "Missing required fields (user, email, password)"}, 400
    except Exception as e:
        print(traceback.format_exc())
        return {"success": False, "message": str(e)}, 500


@app.route('/user', methods=["GET", "OPTIONS"])
def get_user():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}

    user, token = get_user_from_token()

    # Check if the token was valid
    if not token:
        return {"success": False, "message": "Invalid token"}, 401

    return {"success": True, "user": user["user"], "email": user["email"]}


@app.route('/delete_user', methods=["POST", "OPTIONS"])
def delete_user():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}

    user, token = get_user_from_token()

    # Check if the token was valid
    if not token:
        return {"success": False, "message": "Invalid token"}, 401

    # Delete the user
    users.find_one_and_delete({"user_id": user["user_id"]})
    token = request.headers.get("auth-token")

    # Log out the user from this session by deleting the token
    del tokens[token]
    return {"success": True}

if __name__ == '__main__':
    if auth_config["run_on_wsgi"]:
        app.run(debug=auth_config["debug"])
    else:
        app.run(debug=auth_config["debug"], port=auth_config["port"])
