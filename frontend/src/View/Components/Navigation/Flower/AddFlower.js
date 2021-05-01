import React from "react";
import Header from "../../Interface/HeaderAuth";
import AddFlowerForm from "../../Florist-shop/Flower/AddFlowerForm";
import ProfileBack from "../../Florist-shop/Homepage/ProfileBack";
class Add extends React.Component {
  render() {
    return (
      <div className="signIn">
        <Header />
        <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
        <div className="container">
          <AddFlowerForm />
        </div>
      </div>
    );
  }
}

export default Add;
