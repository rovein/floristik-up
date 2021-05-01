import React from "react";
import Input from "../../Interface/Input";
import Button from "../../Interface/Button";
import {withTranslation} from "react-i18next";
import jwt_decode from "jwt-decode";

var url = "http://localhost:8080";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

class AddFlowerForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      color: "",
      shelfLife: "",
      minTemperature: "",
      maxTemperature: "",
      buttonDisabled: false,
    };
  }

  setInputValue(property, val) {
    this.setState({
      [property]: val,
    });
  }

  resetForm() {
    this.setState({
      name: "",
      color: "",
      shelfLife: "",
      minTemperature: "",
      maxTemperature: "",
      buttonDisabled: false,
    });
  }

  checkName(name) {
    let regCity = new RegExp('^([А-Яа-яё]+)|([A-Za-z]+)$');
    if (!regCity.test(name)) {
      this.setState({flag: 7});
      return false
    }
    return true
  }

  checkColor(color) {
    let regStreet = new RegExp('^([А-Яа-яё]+)|([A-Za-z]+)$');
    if (!regStreet.test(color)) {
      this.setState({flag: 8});
      return false
    }
    return true
  }

  checkTemperature(temperature) {
    let regHouse = new RegExp('^\\+?-?[0-9]+$');
    if (!regHouse.test(temperature)) {
      this.setState({flag: 9});
      return false
    }
    return true
  }

  checkShelfLife(value) {
    let editType = new RegExp("^([0-9]+)$");
    if (!editType.test(value)) {
      this.setState({flag: 10});
      return false;
    }
    return true;
  }

  checkCred() {
    if (!this.checkName(this.state.name)) {
      return;
    }
    if (!this.checkColor(this.state.color)) {
      return;
    }
    if (!this.checkTemperature(this.state.minTemperature)) {
      return;
    }
    if (!this.checkTemperature(this.state.maxTemperature)) {
      return;
    }
    if (!this.checkShelfLife(this.state.shelfLife)) {
      return;
    }

    this.setState({
      buttonDisabled: true,
    });

    this.addFlower();
  }

  async addFlower() {
    try {
      let res = await fetch(`${url}/flowers`, {
        method: "post",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
        body: JSON.stringify({
          name: this.state.name,
          color:  this.state.color,
          shelfLife:  this.state.shelfLife,
          minTemperature:  this.state.minTemperature,
          maxTemperature:  this.state.maxTemperature,
        }),
      });
      let result = await res.json();
      if (result) {
        window.location.href = "./profile";
      }
    } catch (e) {
      console.log(e);
      this.resetForm();
    }
  }

  render() {
    const {t} = this.props;
    return (
      <div className="signUpForm">
        <div className="signUpContainer">
          <h1>{t("AddF")}</h1>
          {this.state.flag === 7 && <p>{t("EName")}</p>}
          {this.state.flag === 8 && <p>{t("EColor")}</p>}
          {this.state.flag === 9 && <p>{t("ETemperature")}</p>}
          {this.state.flag === 10 && <p>{t("EShelfLife")}</p>}
          <Input
            type="text"
            placeholder={t("FName")}
            value={this.state.name ? this.state.name : ""}
            onChange={(val) => this.setInputValue("name", val)}
          />
          <Input
            type="text"
            placeholder={t("FColor")}
            value={this.state.color ? this.state.color : ""}
            onChange={(val) => this.setInputValue("color", val)}
          />
          <Input
            type="text"
            placeholder={t("FShelfLife")}
            value={this.state.shelfLife ? this.state.shelfLife : ""}
            onChange={(val) => this.setInputValue("shelfLife", val)}
          />
          <Input
            type="text"
            placeholder={t("FMinTemp")}
            value={this.state.minTemperature ? this.state.minTemperature : ""}
            onChange={(val) => this.setInputValue("minTemperature", val)}
          />
          <Input
            type="text"
            placeholder={t("FMaxTemp")}
            value={this.state.maxTemperature ? this.state.maxTemperature : ""}
            onChange={(val) => this.setInputValue("maxTemperature", val)}
          />
          <Button
            text={t("Add")}
            disabled={this.state.buttonDisabled}
            onClick={() => this.checkCred()}
          />
        </div>
      </div>
    );
  }
}

export default withTranslation()(AddFlowerForm);
