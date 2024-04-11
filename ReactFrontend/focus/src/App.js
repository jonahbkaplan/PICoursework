import './App.css';
import {Login} from './Login';
import {Signup} from './Signup';
import {useState} from "react";

function App() {
  const [currentForm, setCurrentForm] = useState('Login');

  let form;
  if (currentForm === 'Login') {
    form = <Login onFormSwitch = {() => setCurrentForm("Signup")}/>
  } else {
    form = <Signup onFormSwitch = {() => setCurrentForm("Login")}/>
  }

  return (
    <div className="App">
        {form}
    </div>
  );
}

export default App;
