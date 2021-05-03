import React from "react";
import {withTranslation} from "react-i18next";
import ShopCard from "./AdminShopCard";
import Button from "../Interface/Button";

class Profile extends React.Component {

  render() {
    localStorage.removeItem("Email");
    localStorage.removeItem("Role");
    const {t} = this.props;
    return (
      <div>
        <div className="profile_back">
          <p id="cName">{t("Admin")}</p>
          <Button
            style={{marginTop: '1%'}}
            text={t("CreateAcc")}
            disabled={false}
            onClick={() => {
              window.location.href = "/create-account";
            }}
          />
        </div>

        <div className="rooms_back">
          <p id="EMP">{t("FloristShops")}</p>
        </div>
        <div id="rooms_container">
          <ShopCard/>
        </div>
      </div>
    );
  }
}

export default withTranslation()(Profile);
