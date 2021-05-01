import React from 'react'
import Header from '../Interface/Header'
import SignUpForm from "../Authorization/SignUpForm";

class SignUp extends React.Component{
    render() {
        var lng = localStorage.getItem("i18nextLng")
              localStorage.clear();
              localStorage.setItem("i18nextLng", lng)   
        return(
            <div className="signIn">
            <Header/>
                <div className="container">
                    <SignUpForm role="USER"/>
            </div>
        </div>
        )
    }
}

export default SignUp;
