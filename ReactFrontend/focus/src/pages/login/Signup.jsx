import React, {useState} from "react";

export const Signup = (props) => {
    const [user, setUser] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        e.preventDefault();
        console.log(user,email, password);
        fetch("http://localhost:5000/signup", {
            headers: {
                'Content-Type': 'application/json',
            },
            method: 'POST',
            body: JSON.stringify({user,email, password}),
        }).then(response => response.json()).then(data => {
            if (data["success"] === false) {
                console.log("Err:" ,data["message"])
                setErrorMsg(data["message"]);
            } else {
                setErrorMsg("");
            }
        }).catch(err => {
            console.log(err);
        });
    }

    return (
        <div className="AuthContainer">
            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="username" id="user" name="user" onChange={(e) => setUser(e.target.value)}/>
                <input type="email" placeholder="email@gmail.com" id="email" name="email" onChange={(e) => setEmail(e.target.value)}/>
                <input type="password" id="password" name="password" onChange={(e) => setPassword(e.target.value)}/>
                <div>{errorMsg}</div>
                <button type="submit">Sign up</button>
            </form>
            <button onClick={props.onFormSwitch}>Already have an account? Login here</button>
        </div>
    )
}