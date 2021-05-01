import React from "react";
import Header from "../Interface/HeaderAuth";
import EditForm from "../Florist-shop/Homepage/EditForm";
import jwt_decode from "jwt-decode";
import ProfileBack from "../Florist-shop/Homepage/ProfileBack";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

class Edit extends React.Component {
  render() {
    if (
      decoded.role === "USER" ||
      localStorage.getItem("Role") === "USER"
    ) {
      return (
        <div className="signIn">
          <Header />
          <div className="container">
            <EditForm />
          </div>
        </div>
      );
    }
  }
}

export default Edit;
