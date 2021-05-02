import React from "react";
import Header from "../../Interface/HeaderAuth";
import ProfileBack from "../../Florist-shop/Homepage/ProfileBack";
import AddStorageForm from "../../Florist-shop/Storage/AddStorageForm";

class Add extends React.Component {
  render() {
    return (
      <div className="signIn">
        <Header />
        <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
        <div className="container">
          <AddStorageForm />
        </div>
      </div>
    );
  }
}

export default Add;
