import React from "react";
import Button from "../Interface/Button";
import { withTranslation } from "react-i18next";
import DatePicker from "../Interface/DatePicker";
import jwt_decode from "jwt-decode";

var url = "http://localhost:8080";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

class SignInForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pId: localStorage.getItem("procId"),
      cId: localStorage.getItem("cId"),
      date: localStorage.getItem("date"),
    };
  }

  resetForm() {
    this.setState({
      email: "",
      password: "",
      buttonDisabled: false,
    });
  }

  async handleDateSelect() {
    try {
      let res = await fetch(`${url}/schedules`, {
        method: "post",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
        body: JSON.stringify({
          procedureId: this.state.pId,
          customerId: this.state.cId,
          date: new Date(this.state.date),
        }),
      });
      let result = await res.json();
      if (result) {
        window.location.href = "./profile";
      }
    } catch (e) {
      console.log(e);
    }
  }

  render() {
    const { t } = this.props;
    return (
      <div className="signInForm">
        <div className="signInContainer">
          <p>{t("SelectDate")}:</p>
          <DatePicker initialValue={new Date()} />
          <p></p>
          <Button text={t("Pick")} onClick={() => this.handleDateSelect()} />
        </div>
      </div>
    );
  }
}

export default withTranslation()(SignInForm);
