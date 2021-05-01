import React from "react";
import Header from "../Interface/HeaderAuth";
import { withTranslation } from "react-i18next";
import ProfileBack from "../Florist-shop/Homepage/ProfileBack";
import StorageCard from "../Florist-shop/Homepage/Cards/PickStorageCard";

class Info extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false
    };
  }

  render() {
    return <div className="signIn">
      <Header />
      <ProfileBack floristShop={JSON.parse(localStorage.getItem("floristShop"))}/>
      <StorageCard/>
    </div>
  }
}

export default withTranslation()(Info);
