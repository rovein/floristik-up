import React from "react";
import Header from "../../Interface/HeaderAuth";
import ProfileBack from "../../Florist-shop/Homepage/ProfileBack";
import EditStorageForm from "../../Florist-shop/Storage/EditStorageForm";

class Edit extends React.Component {
  render() {
    return (
      <div className="signIn">
        <Header />
        <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
        <div className="container">
          <EditStorageForm />
        </div>
      </div>
    );
  }
}

export default Edit;
