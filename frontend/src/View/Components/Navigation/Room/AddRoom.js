import React from "react";
import Header from "../../Interface/HeaderAuth";
import AddRoomForm from "../../Florist-shop/Room/AddRoomForm";
import ProfileBack from "../../Florist-shop/Homepage/ProfileBack";
class Add extends React.Component {
  render() {
    return (
      <div className="signIn">
        <Header />
        <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
        <div className="container">
          <AddRoomForm />
        </div>
      </div>
    );
  }
}

export default Add;
