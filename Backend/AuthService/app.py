from flask import Flask, request, jsonify
from flask_cors import CORS
import bcrypt
import uuid


# Temporary User Class
class User:
    def __init__(self, user_id, username, pass_hash, salt, email, google_email):
        self.user_id = user_id
        self.username = username
        self.pass_hash = pass_hash
        self.salt = salt
        self.email = email
        self.google_email = google_email
    def __repr__(self):
        return f"User({self.user_id}, {self.username}, {self.pass_hash}, {self.salt}, {self.email}, {self.google_email}), "

users = []
tokens = {}


app = Flask(__name__, )
CORS(app, resources={r"/*": {"origins": ["http://localhost:3000", "http://127.0.0.1:3000"]}})


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
            token = uuid.uuid4()
            tokens[token] = user.user_id
            return {"success": True, "authtoken": token}

        return {"success": False, "message": "Username/Email or password is incorrect"}
    except KeyError:
        return {"success": False, "message": "Missing required fields (useremail, password)"}


@app.route('/signup', methods=["POST"])
def register():
    try:
        if request.method == "OPTIONS":
            return {"Access-Control-Allow-Origin": "*"}
        data = request.get_json()
        if "google_email" not in data:
            data["google_email"] = None

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

        new_user = User(uuid.uuid4(), data["user"], pass_hash, salt, data["email"], data["google_email"])

        print(new_user)

        users.append(new_user)

        # Create token

        token = uuid.uuid4()

        tokens[token] = new_user.user_id

        return {"success": True, "authtoken": token}
    except KeyError:
        return {"success": False, "message": "Missing required fields (user, email, password)"}


if __name__ == '__main__':
    app.run(debug=True, port=5000)
