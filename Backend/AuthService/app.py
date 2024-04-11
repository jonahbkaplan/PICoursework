from flask import Flask, request, jsonify
import datetime
from flask_cors import CORS
import bcrypt
import uuid
import string
from email.utils import parseaddr

alphanum = string.ascii_letters + string.digits
allowedSpecial = "!@#$%^&*_+?"

# Temporary User Class
class User:
    def __init__(self, user_id, username, pass_hash, salt, email):
        self.user_id = user_id
        self.username = username
        self.pass_hash = pass_hash
        self.salt = salt
        self.email = email
    def __repr__(self):
        return f"User({self.user_id}, {self.username}, {self.pass_hash}, {self.salt}, {self.email})"

users = []
tokens = {}

app = Flask(__name__, )
CORS(app, resources={r"/*": {"origins": ["http://localhost:3000", "http://127.0.0.1:3000"]}})

@app.route('/refresh_token', methods=["POST", "OPTIONS"])
def refresh_token():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}
    token = request.headers.get("auth-token")
    if token not in tokens:
        return {"success": False, "message": "Invalid token"}
    user_id = tokens[token][0]
    expiry = datetime.datetime.now() + datetime.timedelta(hours=1)
    tokens[token] = (user_id, expiry)
    return {"success": True}

@app.route('/login', methods=["POST", "OPTIONS"])
def login():
    try:
        if request.method == "OPTIONS":
            return {"Access-Control-Allow-Origin": "*"}
        data = request.get_json()

        user = None
        for u in users:
            if u.username == data["useremail"] or u.email == data["useremail"]:
                user = u
                break

        # Check if the user exists
        if not user:
            return {"success": False, "message": "Username/Email or password is incorrect"}

        pass_hash = bcrypt.hashpw(data["password"].encode(), user.salt.encode()).decode("ascii")

        # Check if the password is correct

        if pass_hash == user.pass_hash:
            # Create token and send a successful login response
            token = str(uuid.uuid4())
            tokens[token] = user.user_id
            return {"success": True, "authtoken": token}

        return {"success": False, "message": "Username/Email or password is incorrect"}
    except KeyError:
        return {"success": False, "message": "Missing required fields (useremail, password)"}

@app.route('/logout', methods=["POST", "OPTIONS"])
def logout():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}
    token = request.headers.get("auth-token")
    if token in tokens:
        del tokens[token]
    return {"success": True}

@app.route('/signup', methods=["POST","OPTIONS"])
def signup():
    try:
        if request.method == "OPTIONS":
            return {"Access-Control-Allow-Origin": "*"}
        data = request.get_json()

        if data["user"] == "" or data["email"] == "" or data["password"] == "":
            raise KeyError()

        if any(char not in alphanum+"_" for char in data["user"]):
            return {"success": False, "message": "Username must only contain alphanumeric characters and _"}

        if parseaddr(data["email"])[1] != data["email"]:
            return {"success": False, "message": "Invalid email format"}

        if len(data["password"]) < 8:
            return {"success": False, "message": "Password must be at least 8 characters long"}

        if any(char not in alphanum + "!@#$%^&*_+?"for char in data["password"]) or not any(char in allowedSpecial for char in data["password"]):
            return {"success": False, "message": f"Password must have alphanumeric characters and at least one special character: {allowedSpecial}"}

        # Check if the username is already taken
        if data["user"] in (user.username for user in users):
            return {"success": False, "message": "Username already taken"}
        if data["email"] in (user.email for user in users):
            return {"success": False, "message": "Email already taken"}

        # Hash the password
        salt = bcrypt.gensalt()
        pass_hash = bcrypt.hashpw(data["password"].encode(), salt).decode("ascii")
        salt = salt.decode("ascii")

        # Create a new user

        new_user = User(str(uuid.uuid4()), data["user"], pass_hash, salt, data["email"])

        users.append(new_user)

        # Create token

        token = str(uuid.uuid4())
        expiry = datetime.datetime.now() + datetime.timedelta(hours=1)

        tokens[token] = (new_user.user_id, expiry)

        return {"success": True, "authtoken": token}
    except KeyError:
        return {"success": False, "message": "Missing required fields (user, email, password)"}

def get_user_from_token():
    token = request.headers.get("auth-token")
    if token not in tokens:
        return None
    if datetime.datetime.now() > tokens[token][1]:
        del tokens[token]
        return None
    user_id = tokens[token][0]
    for u in users:
        if u.user_id == user_id:
            return u
    return None

@app.route('/user', methods=["GET","OPTIONS"])
def get_user():
    if request.method == "OPTIONS":
        return {"Access-Control-Allow-Origin": "*"}

    user = get_user_from_token()

    if not user:
        return {"success": False, "message": "Invalid token"}

    return {"success": True, "user": user.username, "email": user.email}

@app.route('/metrics', methods=["PUT"])
def get_current_metrics():
    try:
        user = get_user_from_token()
        if not user:
            return {"success": False, "message": "Invalid token"}
        data = request.get_json()

    except KeyError:
        return {"success": False, "message": "Missing required fields (google_email)"}

if __name__ == '__main__':
    app.run(debug=True, port=5000)
