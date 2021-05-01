import React from "react";
import Button from "../../Interface/Button";
import {withTranslation} from "react-i18next";

class ProfileBack extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    const {t} = this.props;
    return <div className="profile_back">
      <p id="cName">{this.props.floristShop.name}</p>
      <p></p>
      <p>
        {t("Email")}: {this.props.floristShop.email}
      </p>
      <Button
        text={t("AddS")}
        disabled={false}
        onClick={(e) => {
          window.location.href = "./add_room";
        }}
      />
      <p>
        {t("Phone")}: {this.props.floristShop.phoneNumber}
      </p>
      <Button
        text={t("AddF")}
        disabled={false}
        onClick={() => {
          window.location.href = "./add_flower";
        }}
      />
      <p>
        {t("Country")}: {this.props.floristShop.country}
      </p>
      <Button
        text={t("EditP")}
        disabled={false}
        onClick={() => {
          window.location.href = "./edit";
        }}
      />
    </div>
  }
}

export default withTranslation()(ProfileBack);
