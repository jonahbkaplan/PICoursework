import React, {useState} from "react";

export const Login = (props) => {

    const [useremail, setUserEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMsg, setErrorMsg] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        fetch("http://localhost:5000/login", {
            headers: {
                'Content-Type': 'application/json',
            },
            method: 'POST',
            body: JSON.stringify({useremail, password}),
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
                <input type="text" placeholder="username/email@gmail.com" id="useremail" name="useremail" onChange={(e) => setUserEmail(e.target.value)}/>
                <input type="password" id="password" name="password" onChange={(e) => setPassword(e.target.value)}/>
                <button type="submit">Login</button>
            </form>
            <div>{errorMsg}</div>
            <button onClick={props.onFormSwitch}>Don't have an account? Sign up here</button>
        </div>
    )
}