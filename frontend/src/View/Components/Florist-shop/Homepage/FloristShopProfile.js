import React from "react";
import Button from "../../Interface/Button";
import {withTranslation} from "react-i18next";

import jwt_decode from "jwt-decode";
import PickRoomCard from "./Cards/PickRoomCard";
import PickFlowerCard from "./Cards/PickFlowerCard";
import ProfileBack from "./ProfileBack";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

const url = "http://localhost:8080";

class Profile extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      floristShop: {},
      isChecked: false
    };

    this._handleChange = this._handleChange.bind(this)
  }

  componentWillMount () {
    this.setState( { isChecked: false } );
  }

  componentDidMount() {
    let cachedShop = localStorage.getItem("floristShop");
    if (cachedShop != null) {
      console.log("In cached shop")
      this.setState({
        isLoaded: true,
        floristShop: JSON.parse(cachedShop)
      });
      return
    }

    console.log("Performing fetch...")
    fetch(`${url}/florist-shops/${decoded.email}`, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("Token"),
      },
    })
      .then((res) => res.json())
      .then(
        (result) => {
          console.log("In fetch result")
          if (result.error && result.message === "JWT Token has expired") {
            var lng = localStorage.getItem("i18nextLng");
            localStorage.clear();
            localStorage.setItem("i18nextLng", lng);
            window.location.href = "/";
          }
          this.setState({
            isLoaded: true,
            floristShop: result
          });
          localStorage.setItem("floristShop", JSON.stringify(result))
        },
        (error) => {
          console.log("In error")
          this.setState({
            isLoaded: true,
            error,
          });
        }
      );
  }

  render() {
    localStorage.removeItem("Id");
    const {t} = this.props;
    if (localStorage.getItem("Token") == null) {
      window.location.href = "./";
    } else {
      return (
        <div className="profile">
          <ProfileBack floristShop={this.state.floristShop}/>
          <div className="rooms_back">
            <p>{this.state.isChecked ? t("Flowers") : t("Rooms")}</p>
            <div className="switch-container">
              <label>
                <input ref="switch" checked={ this.state.isChecked } onChange={ this._handleChange } className="switch" type="checkbox" />
                <div>
                  <div></div>
                </div>
              </label>
            </div>
          </div>

          <div id="rooms_container">
            { this.state.isChecked ? <PickFlowerCard/> : <PickRoomCard/> }
          </div>
        </div>
      );
    }
  }

  _handleChange () {
    this.setState( { isChecked: !this.state.isChecked } );
  }

}

export default withTranslation()(Profile);
