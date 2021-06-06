import React from "react";
import Input from "../../Interface/Input";
import Button from "../../Interface/Button";
import { withTranslation } from "react-i18next";
import jwt_decode from "jwt-decode";
import Loader from "react-loader-spinner";

var url = "http://localhost:8080";
if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

class EditForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      email: "",
      phone: "",
      country: "",
      id: "",
      password: "",
      confirmPass: '',
      flag: 1,
      buttonDisabled: false,
      isLoaded: false
    };
  }

  setInputValue(property, val) {
    this.setState({
      [property]: val,
    });
  }

  resetForm() {
    this.setState({
      password: "",
      confirmPass: '',
      buttonDisabled: false,
      isLoaded: true
    });
  }

  componentDidMount() {
    if (decoded.role === "USER") {
      this.getData(`${url}/florist-shops/${decoded.email}`);
    } else if (decoded.role === "ADMIN") {
      if (localStorage.getItem("Role") === "USER") {
        this.getData(`${url}/florist-shops/${localStorage.getItem("Email")}`);
      }
    }
  }

  getData(resUrl) {
    fetch(resUrl, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            name: result.name,
            email: result.email,
            phone: result.phoneNumber,
            country: result.country,
            id: result.id,
            company: result,
            isLocked: result.isLocked
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error,
          });
        }
      );
  }

  checkEmail(email) {
    let regEmail = new RegExp(
      "^([a-z0-9_-]+.)*[a-z0-9_-]+@[a-z0-9_-]+(.[a-z0-9_-]+)*.[a-z]{2,6}$"
    );
    if (!regEmail.test(email)) {
      this.setState({ flag: 2 });
      return false;
    }
    return true;
  }

  checkPass(password) {
    if (password.length < 8) {
      this.setState({ flag: 3 });
      return false;
    }
    return true;
  }

  checkName(name) {
    let regName = new RegExp("^([А-ЯЁа-яё0-9]+)|([A-Za-z0-9]+)$");
    if (!regName.test(name)) {
      this.setState({ flag: 4 });
      return false;
    }
    return true;
  }

  checkPhone(phone) {
    let regPhone = new RegExp("^[+]*[(]{0,1}[0-9]{1,4}[)]{0,1}[-s./0-9]*$");
    if (!regPhone.test(phone)) {
      this.setState({ flag: 5 });
      return false;
    }
    return true;
  }

  checkCountry(country) {
    let regCountry = new RegExp("^([а-яА-Яё]+)|([A-Za-z]+)$");
    if (!regCountry.test(country)) {
      this.setState({ flag: 6 });
      return false;
    }
    return true;
  }

  checkPasswords(password, confirmPassword, context) {
    if (password !== confirmPassword) {
      context.setState({flag: 11});
      return false
    }
    return true
  }

  checkCred() {
    if (!this.checkName(this.state.name)) {
      return;
    }
    if (!this.checkEmail(this.state.email)) {
      return;
    }
    if (this.state.password !== '' && !this.checkPass(this.state.password)) {
      return;
    }
    if (!this.checkPhone(this.state.phone)) {
      return;
    }
    if (!this.checkCountry(this.state.country)) {
      return;
    }
    if (!this.checkPasswords(this.state.password, this.state.confirmPass, this)) {
      return
    }

    this.setState({
      buttonDisabled: true,
      isLoaded: false
    });

    this.editCompany(`${url}/florist-shops`);
  }

  async editCompany(resUrl) {
    try {
      var role;
      if (decoded.role !== "ADMIN") {
        role = decoded.role;
      } else {
        role = localStorage.getItem("Role");
      }

      let requestBody = {
        name: this.state.name,
        email: this.state.email,
        country: this.state.country,
        id: this.state.id,
        phoneNumber: this.state.phone,
        isLocked: this.state.isLocked,
        role: role,
      };

      if (this.state.password) {
        requestBody.password = this.state.password;
      }

      console.log(requestBody)

      let res = await fetch(resUrl, {
        method: "put",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
        body: JSON.stringify(requestBody),
      });
      let result = await res.json();
      console.log(result)
      if (result && result.id !== null) {
        localStorage.setItem("floristShop", JSON.stringify(result));
        window.location.href = "./profile";
      } else if (result) {
        this.resetForm();
        this.setState({ flag: 10 });
      }
    } catch (e) {
      console.log(e);
      this.resetForm();
    }
  }

  render() {
    const { t } = this.props;
    if (!this.state.isLoaded) {
      return <div>
        <Loader
          type="BallTriangle"
          color="seagreen"
          height={500}
          width={500}
          timeout={10000}
        />
      </div>;
    }
    return (

      <div className="signUpForm">
        <div className="signUpContainer">
          <h1>{t("Edit")}</h1>
          {this.state.flag === 2 && <p>{t("EEmail")}</p>}
          {this.state.flag === 3 && <p>{t("EPass")}</p>}
          {this.state.flag === 4 && <p>{t("EName")}</p>}
          {this.state.flag === 5 && <p>{t("EPhone")}</p>}
          {this.state.flag === 6 && <p>{t("ECountry")}</p>}
          {this.state.flag === 7 && <p>{t("ECity")}</p>}
          {this.state.flag === 8 && <p>{t("EStreet")}</p>}
          {this.state.flag === 9 && <p>{t("EHouse")}</p>}
          {this.state.flag === 10 && <p>{t("EError")}</p>}
          { this.state.flag === 11 && <p>{t("EConfirmPass")}</p>}
          <Input
            type="text"
            placeholder={t("DName")}
            value={this.state.name ? this.state.name : ""}
            onChange={(val) => this.setInputValue("name", val)}
          />
          {this.state.flag === 2 && (
            <p>
              Your login credentials could not be verified, please try again.
            </p>
          )}
          <Input
            type="text"
            disabled = {true}
            placeholder={t("Email")}
            value={this.state.email ? this.state.email : ""}
            onChange={(val) => this.setInputValue("email", val)}
          />
          <Input
            type="text"
            placeholder={t("Phone")}
            value={this.state.phone ? this.state.phone : ""}
            onChange={(val) => this.setInputValue("phone", val)}
          />
          <Input
            type="text"
            placeholder={t("FCountry")}
            value={this.state.country ? this.state.country : ""}
            onChange={(val) => this.setInputValue("country", val)}
          />
          <Input
              type="password"
              placeholder={t("Password")}
              value={this.state.password ? this.state.password : ""}
              onChange={(val) => this.setInputValue("password", val)}
          />
          <Input
              type = 'password'
              placeholder = {t('ConfirmPassword')}
              value={this.state.confirmPass ? this.state.confirmPass : ''}
              onChange = { (val) => this.setInputValue('confirmPass', val)}
          />
          <Button
            text={t("Save")}
            disabled={this.state.buttonDisabled}
            onClick={() => this.checkCred()}
          />
        </div>
      </div>
    );
  }
}

export default withTranslation()(EditForm);
