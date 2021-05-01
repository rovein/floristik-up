import React from 'react'
import Input from '../Interface/Input'
import Button from '../Interface/Button'
import { withTranslation } from 'react-i18next'

var url = "http://localhost:8080";

class SignInForm extends React.Component{
    constructor(props) {
        super(props)
        this.state = {
            email:'',
            password:'',
            buttonDisabled: false,
            flag: 1
        }
    }

    setInputValue(property, val) {
        this.setState({
            [property]: val
        })
    }

    resetForm(){
        this.setState({
            email: '',
            password: '',
            buttonDisabled: false
        })
    }

    checkEmail(email) {
        let regEmail = new RegExp('^([a-z0-9_-]+.)*[a-z0-9_-]+@[a-z0-9_-]+(.[a-z0-9_-]+)*.[a-z]{2,6}$');
        if(!regEmail.test(email)){
            this.setState({flag: 2});
            return false
        }
        return true
    }

    checkPass(password) {
        if(password.length < 8){
            this.setState({flag: 3});
            return false
        }
        return true
    }

    async doSignIn() {
        if(!this.checkEmail(this.state.email, this)){
            return
        }
        if(!this.checkPass(this.state.password, this)){
            return
        }
        this.setState({
            buttonDisabled: true
        })
        try{
            let res = await fetch(`${url}/auth/login`, {
                method: 'post',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: this.state.email,
                    password: this.state.password
                })
            })
            let result = await res.json()
            if(result && result.message !== "Access Denied"){
                localStorage.setItem('Token', result.token);
                window.location.href='./profile';
            } else if (result) {
                this.resetForm()
                this.setState({flag: 4}); 
            }
        }
        catch(e){
            console.log(e)
            this.resetForm()
        }
    }

    render() {
        const {t} = this.props
        return(
            <div className="signInForm">
                <div className='signInContainer'>
                    <h1>{t('Login')}</h1>
                    { this.state.flag === 2 && <p>{t("EEmail")}</p>}
                    { this.state.flag === 3 && <p>{t("EPass")}</p>}
                    { this.state.flag === 4 && <p>{t("checkCred")}</p>}
                    <Input
                        type = 'text'
                        placeholder = {t('Email')}
                        value={this.state.email ? this.state.email : ''}
                        onChange = { (val) => this.setInputValue('email', val)}
                    />
                    <Input
                        type = 'password'
                        placeholder = {t('Password')}
                        value={this.state.password ? this.state.password : ''}
                        onChange = { (val) => this.setInputValue('password', val)}
                    />
                    <Button
                        text = {t('Login')}
                        disabled = {this.state.buttonDisabled}
                        onClick = { () => this.doSignIn()}
                    />
                </div>
            </div>
        )
    }
}

export default withTranslation() (SignInForm);
