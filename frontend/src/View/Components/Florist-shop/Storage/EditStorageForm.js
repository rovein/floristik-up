import React from 'react';
import {withTranslation} from "react-i18next";
import jwt_decode from "jwt-decode";
import Loader from "react-loader-spinner";
import Input from "../../Interface/Input";
import Button from "../../Interface/Button";

var url = "http://localhost:8080";
if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

class EditStorageForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      flowerName: '',
      flowerColor: '',
      flowerShelfLife: '',
      city: '',
      street: '',
      house: '',
      startDate: '',
      amount: '',
      flag: 1,
      buttonDisabled: false,
      isLoaded: false,
      error: ''
    };
  }

  setInputValue(property, val) {
    this.setState({
      [property]: val,
    });
  }

  componentDidMount() {
    fetch(`${url}/flower-storages/info/${localStorage.getItem("SId")}`, {
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
            flowerName: result.flowerName,
            flowerColor: result.flowerColor,
            flowerShelfLife: result.flowerShelfLife,
            city: result.city,
            street: result.street,
            house: result.house,
            startDate: result.formattedDate,
            amount: result.amount,
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

  checkAmount(amount) {
    let editType = new RegExp("^([0-9]+)$");
    if (!editType.test(amount)) {
      this.setState({flag: 10});
      return false;
    }
    return true;
  }

  checkCred() {
    if (!this.checkAmount(this.state.amount)) {
      return;
    }

    this.setState({
      buttonDisabled: true
    });

    this.editStorage();
  }

  async editStorage() {
    try {
      let res = await fetch(`${url}/flower-storages`, {
        method: "put",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
        body: JSON.stringify({
          id: localStorage.getItem("SId"),
          amount: this.state.amount
        }),
      });
      let result = await res.json();
      if (result && result.error) {
        this.setState({ flag: 11, error: result.error, buttonDisabled: false });
      } else if (result && result.id !== null) {
        localStorage.removeItem("SId");
        window.location.href = "./more";
      }
    } catch (e) {
      console.log(e);
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
          {this.state.flag === 10 && <p>{t("EAmount")}</p>}
          {this.state.flag === 11 && <p className="text-danger">{this.state.error}</p>}
          <Input
            type="text"
            value={this.state.flowerName}
            disabled={true}
          />
          <Input
            type="text"
            value={this.state.flowerColor}
            disabled={true}
          />
          <Input
            type="text"
            value={this.state.flowerShelfLife}
            disabled={true}
          />
          <Input
            type="text"
            value={this.state.city + ', ' + this.state.street + ' ' + this.state.house}
            disabled={true}
          />
          <Input
            type="text"
            value={this.state.startDate}
            disabled={true}
          />
          <Input
            type="text"
            placeholder={t("Amount")}
            value={this.state.amount ? this.state.amount : ""}
            onChange={(val) => this.setInputValue("amount", val)}
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

export default withTranslation()(EditStorageForm);
