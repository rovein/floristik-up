import React from 'react'
import Header from '../Interface/Header'
import LoginForm from '../Authorization/LoginForm'
class Login extends React.Component{
    
    render() { 
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <LoginForm/>
            </div>
        </div>
        )
    }
}

export default Login;
