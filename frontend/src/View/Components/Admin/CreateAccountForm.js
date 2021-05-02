import React from 'react'
import Input from '../Interface/Input'
import Button from '../Interface/Button'
import { withTranslation } from 'react-i18next'
import Loader from "react-loader-spinner";

let url = "http://localhost:8080";

class CreateAccountForm extends React.Component {

  constructor(props) {
    super(props)
    this.state = {
      name: '',
      email: '',
      phone: '',
      country: '',
      password: '',
      confirmPass: '',
      flag: 1,
      buttonDisabled: false,
      isLoaded: true
    }
  }

  setInputValue(property, val) {
    this.setState({
      [property]: val
    })
  }

  resetForm(){
    this.setState({
      name: '',
      email: '',
      phone:'',
      country:'',
      password: '',
      confirmPass: '',
      buttonDisabled: false,
      isLoaded: true
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

  checkPhone(phone) {
    let regPhone = new RegExp('^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-s./0-9]*$');
    if(!regPhone.test(phone)){
      this.setState({flag: 5});
      return false
    }
    return true
  }

  checkPasswords(password, confirmPassword) {
    if (password !== confirmPassword) {
      this.setState({flag: 11});
      return false
    }
    return true
  }

  checkCred() {
    if (!this.checkEmail(this.state.email)) {
      return
    }
    if (!this.checkPass(this.state.password)) {
      return
    }
    if (!this.checkPhone(this.state.phone)) {
      return
    }
    if (!this.checkPasswords(this.state.password, this.state.confirmPass)) {
      return
    }

    this.setState({
      buttonDisabled: true,
      isLoaded: false
    })

    this.doSignUp()
  }

  async doSignUp() {
    console.log("Sending request")
    try{
      let res = await fetch(`${url}/auth/register/florist-shop`, {
        method: 'post',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          name: this.state.name,
          email: this.state.email,
          creationDate: new Date(),
          phoneNumber: this.state.phone,
          role: 'USER',
          password: this.state.password,
          country: this.state.country
        })
      })
      let result = await res.json()
      if (res.status === 400 && result.email) {
        this.setState({flag: 10, buttonDisabled: false});
      } else if(result && result.id !== null) {
        window.location.href='./profile';
      } else if (result){
        this.resetForm()
        this.setState({flag: 9});
      }
    }
    catch(e){
      console.log(e)
      this.resetForm()
    }
  }

  render() {
    const {t} = this.props
    if (!this.state.isLoaded) {
      return <div>
        <Loader
          type="BallTriangle"
          color="seagreen"
          height={400}
          width={400}
          timeout={10000}
        />
      </div>;
    }
    return(
      <div className="signUpForm">
        <div className='signUpContainer'>
          <h1>{t('CreateAcc')}</h1>
          { this.state.flag === 2 && <p>{t("EEmail")}</p>}
          { this.state.flag === 3 && <p>{t("EPass")}</p>}
          { this.state.flag === 4 && <p>{t("EName")}</p>}
          { this.state.flag === 5 && <p>{t("EPhone")}</p>}
          { this.state.flag === 6 && <p>{t("ECountry")}</p>}
          { this.state.flag === 10 && <p>{t("eExist")}</p>}
          { this.state.flag === 11 && <p>{t("EConfirmPass")}</p>}
          <Input
            type = 'text'
            placeholder = {t('DName')}
            value={this.state.name ? this.state.name : ''}
            onChange = { (val) => this.setInputValue('name', val)}
          />
          <Input
            type = 'text'
            placeholder = {t('Email')}
            value={this.state.email ? this.state.email : ''}
            onChange = { (val) => this.setInputValue('email', val)}
          />
          <Input
            type = 'text'
            placeholder = {t('Phone')}
            value={this.state.phone ? this.state.phone : ''}
            onChange = { (val) => this.setInputValue('phone', val)}
          />
          <Input
            type = 'text'
            placeholder = {t('FCountry')}
            value={this.state.country ? this.state.country : ''}
            onChange = { (val) => this.setInputValue('country', val)}
          />
          <Input
            type = 'password'
            placeholder = {t('Password')}
            value={this.state.password ? this.state.password : ''}
            onChange = { (val) => this.setInputValue('password', val)}
          />
          <Input
            type = 'password'
            placeholder = {t('ConfirmPassword')}
            value={this.state.confirmPass ? this.state.confirmPass : ''}
            onChange = { (val) => this.setInputValue('confirmPass', val)}
          />
          <Button
            text = {t('Signup')}
            disabled = {this.state.buttonDisabled}
            onClick = { () => this.checkCred()}
          />
        </div>
      </div>
    )
  }
}

export default withTranslation() (CreateAccountForm);
