import React from "react";
import Header from "../Interface/HeaderAuth";
import CreateAccountForm from "../Admin/CreateAccountForm";

class CreateAccount extends React.Component {
  render() {
    return (
      <div className="profile">
        <Header/>
        <div className="container">
          <CreateAccountForm/>
        </div>
      </div>
    );
  }
}

export default (CreateAccount);


