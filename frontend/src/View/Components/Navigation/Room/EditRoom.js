import React from "react";
import Header from "../../Interface/HeaderAuth";
import EditRoomForm from "../../Florist-shop/Room/EditRoomForm";
import ProfileBack from "../../Florist-shop/Homepage/ProfileBack";
class Edit extends React.Component {
  render() {
    return (
      <div className="signIn">
        <Header />
        <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
        <div className="container">
          <EditRoomForm />
        </div>
      </div>
    );
  }
}

export default Edit;
