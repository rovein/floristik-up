import React from "react";
import Header from "../Interface/HeaderAuth";
import { withTranslation } from "react-i18next";
import CleaningCard from "../Florist-shop/ProviderCard";
class Search extends React.Component {
  render() {
    const { t } = this.props;
    return (
      <div className="signIn">
        <Header />
        <div id="rooms_container">
          <CleaningCard />
        </div>
      </div>
    );
  }
}

export default withTranslation()(Search);
