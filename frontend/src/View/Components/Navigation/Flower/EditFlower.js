import React from "react";
import Header from "../../Interface/HeaderAuth";
import EditFlowerForm from "../../Florist-shop/Flower/EditFlowerForm";
import ProfileBack from "../../Florist-shop/Homepage/ProfileBack";

class Edit extends React.Component {
  render() {
    return (
      <div className="signIn">
        <Header />
        <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
        <div className="container">
          <EditFlowerForm />
        </div>
      </div>
    );
  }
}

export default Edit;
