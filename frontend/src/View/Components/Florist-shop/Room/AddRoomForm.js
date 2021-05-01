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

class AddSForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      city: "",
      street: "",
      house: "",
      maxCapacity: "",
      buttonDisabled: false,
    };
  }

  setInputValue(property, val) {
    val = val.trim();
    this.setState({
      [property]: val,
    });
  }

  resetForm() {
    this.setState({
      city: "",
      street: "",
      house: "",
      maxCapacity: "",
      buttonDisabled: false,
    });
  }

  checkCity(city) {
    let regCity = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
    if (!regCity.test(city)) {
      this.setState({flag: 7});
      return false
    }
    return true
  }

  checkStreet(street) {
    let regStreet = new RegExp('^([А-Яа-яё]+)|([a-z]+)$');
    if (!regStreet.test(street)) {
      this.setState({flag: 8});
      return false
    }
    return true
  }

  checkHouseNum(house) {
    let regHouse = new RegExp('^([0-9]+)$');
    if (!regHouse.test(house)) {
      this.setState({flag: 9});
      return false
    }
    return true
  }

  checkMaxAmount(amount) {
    let editType = new RegExp("^([0-9]+)$");
    if (!editType.test(amount)) {
      this.setState({flag: 10});
      return false;
    }
    return true;
  }

  checkCred() {
    if (!this.checkCity(this.state.city)) {
      return;
    }
    if (!this.checkStreet(this.state.street)) {
      return;
    }
    if (!this.checkHouseNum(this.state.house)) {
      return;
    }
    if (!this.checkMaxAmount(this.state.maxCapacity)) {
      return;
    }
    this.setState({
      buttonDisabled: true,
    });

    this.addRoom();
  }

  async addRoom() {
    try {
      let res = await fetch(`${url}/florist-shops/${decoded.email}/rooms`, {
        method: "post",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
        body: JSON.stringify({
          city: this.state.city,
          street: this.state.street,
          house: this.state.house,
          maxCapacity: this.state.maxCapacity
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
          <h1>{t("AddS")}</h1>
          {this.state.flag === 7 && <p>{t("ECity")}</p>}
          {this.state.flag === 8 && <p>{t("EStreet")}</p>}
          {this.state.flag === 9 && <p>{t("EHouse")}</p>}
          {this.state.flag === 10 && <p>{t("EMaxCapacity")}</p>}
          <Input
            type="text"
            placeholder={t("FCity")}
            value={this.state.city ? this.state.city : ""}
            onChange={(val) => this.setInputValue("city", val)}
          />

          <Input
            type="text"
            placeholder={t("FStreet")}
            value={this.state.street ? this.state.street : ""}
            onChange={(val) => this.setInputValue("street", val)}
          />
          <Input
            type="text"
            placeholder={t("FHouse")}
            value={this.state.house ? this.state.house : ""}
            onChange={(val) => this.setInputValue("house", val)}
          />
          <Input
            type="text"
            placeholder={t("FMaxCapacity")}
            value={this.state.maxCapacity ? this.state.maxCapacity : ""}
            onChange={(val) => this.setInputValue("maxCapacity", val)}
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

export default withTranslation()(AddSForm);
