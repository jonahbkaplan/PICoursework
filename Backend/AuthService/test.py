import requests

# Testing the main authentication service API endpoints.

def test_signup():
    # Checking well-formed input
    response = requests.post('http://localhost:5000/signup', json = {"user": "testingpass", "email": "test@mail.com", "password": "test"})
    assert response.status_code == 200
    assert response.json()["success"]

    # Checking invalid password
    response = requests.post('http://localhost:5000/signup', json = {"user": "test", "email": "test@mail.com", "password": "test"})
    assert response.status_code == 400
    assert not response.json()["success"]

    # Checking invalid email
    response = requests.post('http://localhost:5000/signup', json = {"user": "test", "email": "test", "password": "test"})
    assert response.status_code == 400
    assert not response.json()["success"]

    # Checking invalid username
    response = requests.post('http://localhost:5000/signup', json = {"user": "4", "email": "test@mail.com", "password": "test"})
    assert response.status_code == 400
    assert not response.json()["success"]

    # Checking total malformed input
    response = requests.post('http://localhost:5000/signup', json = {})
    assert response.status_code == 400
    assert not response.json()["success"]


# Unified test for (refresh_token, user, delete_user and logout endpoints).
def test_refresh_token_deletion_user_logout():
    # We use an existing entry (user=test, user_id=12) in the DB with a pre-generated authentication token to check if the endpoint accepts it.
    response = requests.get('http://localhost:5000/refresh_token', headers={"auth_token": "..."})
    assert response.status_code == 200
    assert response.json()["success"]

    # We use another existing entry (user=test1, user_id=13) in the DB with another pre-generated authentication token, but a false is provided to test if the endpoint rejects it.
    response = requests.get('http://localhost:5000/refresh_token', headers={"auth_token": "..."})
    assert response.status_code == 400
    assert not response.json()["success"]



def test_login():   
    # Check fully malformed input
    response = requests.post('http://localhost:5000/login', json = {})
    assert response.status_code == 400
    assert not response.json()["success"]
    # Test rejects invalid password with test entry ()
    response = requests.post('http://localhost:5000/login', json = {"useremail": "test", "password": "test"})
    assert response.status_code == 400
    assert not response.json()["success"]

    # Test rejects invalid user/email with test entry ()
    response = requests.post('http://localhost:5000/login', json = {"useremail": "test", "password": "test"})
    assert response.status_code == 400
    assert not response.json()["success"]

    # Test accepts valid password with test entry ()
    response = requests.post('http://localhost:5000/login', json = {"useremail": "test", "password": "test"})
    assert response.status_code == 200
    assert response.json()["success"]

    # Test accepts valid user/email with test entry ()
    response = requests.post('http://localhost:5000/login', json = {"useremail": "test", "password": "test"})
    assert response.status_code == 200
    assert response.json()["success"]

