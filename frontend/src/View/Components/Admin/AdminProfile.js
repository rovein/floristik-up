import React from "react";
import {withTranslation} from "react-i18next";
import ShopCard from "./AdminShopCard";
import Button from "../Interface/Button";
import axios from "axios";

var url = "http://localhost:8080";
const FileDownload = require("js-file-download");

class Profile extends React.Component {

  backup() {
    axios({
      url: `${url}/admin/backup`,
      method: "GET",
      headers: {
        Accept: "application/octet-stream",
        "Content-Type": "application/octet-stream",
        "Content-Disposition": "attachment; filename='backup_data.sql'",
        Authorization: "Bearer " + localStorage.getItem("Token"),
      },
      responseType: "blob", // Important
    }).then((response) => {
      FileDownload(response.data, "backup_data.sql");
    });
  }

  render() {
    localStorage.removeItem("Email");
    localStorage.removeItem("Role");
    const {t} = this.props;
    return (
      <div>
        <div className="profile_back">
          <p id="cName">{t("Admin")}</p>
          <Button
            text={t("Backup")}
            disabled={false}
            onClick={(e) => {
              this.backup();
            }}
          />
        </div>

        <div className="rooms_back">
          <p id="EMP">{t("CComp")}</p>
        </div>
        <div id="rooms_container">
          <ShopCard/>
        </div>
      </div>
    );
  }
}

export default withTranslation()(Profile);
