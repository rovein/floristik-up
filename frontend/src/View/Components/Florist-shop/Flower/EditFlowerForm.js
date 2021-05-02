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

class EditFlowerForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      name: "",
      color: "",
      shelfLife: "",
      minTemperature: "",
      maxTemperature: "",
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
      buttonDisabled: false,
      isLoaded: true
    });
  }

  componentDidMount() {
    fetch(`${url}/flowers/${localStorage.getItem("FId")}`, {
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
            color:  result.color,
            shelfLife: result.shelfLife,
            minTemperature:  result.minTemperature,
            maxTemperature:  result.maxTemperature,
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
      isLoaded: false
    });

    this.editFlower();
  }

  async editFlower() {
    try {
      let res = await fetch(`${url}/flowers`, {
        method: "put",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
        body: JSON.stringify({
          id: localStorage.getItem("FId"),
          name: this.state.name,
          color:  this.state.color,
          shelfLife:  this.state.shelfLife,
          minTemperature:  this.state.minTemperature,
          maxTemperature:  this.state.maxTemperature,
        }),
      });
      let result = await res.json();
      if (result && result.id !== null) {
        localStorage.removeItem("FId");
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
          height={400}
          width={400}
          timeout={10000}
        />
      </div>;
    }
    return (
      <div className="signUpForm">
        <div className="signUpContainer">
          <h1>{t("Edit")}</h1>
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
            text={t("Save")}
            disabled={this.state.buttonDisabled}
            onClick={() => this.checkCred()}
          />
        </div>
      </div>
    );
  }
}

export default withTranslation()(EditFlowerForm);
