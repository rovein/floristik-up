import React from 'react';
import {withTranslation} from "react-i18next";
import Loader from "react-loader-spinner";
import Input from "../../Interface/Input";
import Button from "../../Interface/Button";
import jwt_decode from "jwt-decode";
import Select from 'react-select'

var url = "http://localhost:8080";
if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

class AddStorageForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      flowers: [],
      selectOptions : [],
      flowerId: '',
      flowerName: '',
      address: localStorage.getItem("Address"),
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

  handleChange(e) {
    this.setState({flowerId: e.value, flowerName: e.label})
  }

  componentDidMount() {
    fetch(`${url}/flowers`, {
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
          const options = result.map(d => ({
            "value" : d.id,
            "label" : d.name + ", " + d.color
          }))
          this.setState({
            isLoaded: true,
            flowers: result,
            selectOptions: options
          });
          console.log(options)
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
      buttonDisabled: true,
      isLoaded: false
    });

    this.addStorage();
  }

  async addStorage() {
    try {
      let res = await fetch(`${url}/flower-storages`, {
        method: "post",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
        body: JSON.stringify({
          amount: this.state.amount,
          flowerId: this.state.flowerId,
          storageRoomId: localStorage.getItem("RId")
        }),
      });
      let result = await res.json();
      if (result && result.error) {
        console.log(result)
        this.setState({ flag: 11, error: result.error, buttonDisabled: false, isLoaded: true });
      } else if (result && result.id !== null) {
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
          <h1>{t("Add")}</h1>
          {this.state.flag === 10 && <p>{t("EAmount")}</p>}
          {this.state.flag === 11 && <p className="text-danger">{this.state.error}</p>}
          <Input
            type="text"
            value={this.state.address}
            disabled={true}
          />
          <Input
            type="text"
            placeholder={t("Amount")}
            value={this.state.amount ? this.state.amount : ""}
            onChange={(val) => this.setInputValue("amount", val)}
          />
          <Select options={this.state.selectOptions} onChange={this.handleChange.bind(this)} />
          <Button
            style={{marginTop: '15px'}}
            text={t("Add")}
            disabled={this.state.buttonDisabled}
            onClick={() => this.checkCred()}
          />
        </div>
      </div>
    );
  }

}

export default withTranslation()(AddStorageForm);
