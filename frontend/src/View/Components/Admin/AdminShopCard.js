import React from "react";
import Button from "../Interface/Button";
import {withTranslation} from "react-i18next";

var url = "http://localhost:8080";

class Card extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      floristShops: {},
    };
  }

  componentDidMount() {
    fetch(`${url}/florist-shops`, {
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
          this.setState({
            isLoaded: true,
            floristShops: result,
          });
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error,
          });
        }
      );
  }

  render() {
    const {t} = this.props;
    const {error, isLoaded, floristShops} = this.state;
    if (error) {
      return (
        <div className="additional">
          {t("Failiture")}: {error.message}
        </div>
      );
    } else if (!isLoaded) {
      return <p>Loading</p>;
    } else {
      return <div className="grid">{floristShops.map(this.renderCard)}</div>;
    }
  }

  deleteCompany(email) {
    const {t} = this.props;

    if (window.confirm(t("areYouSure"))) {
      fetch(`${url}/florist-shops/${email}`, {
        method: "delete",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
      }).then(
        (result) => {
          window.location.reload();
        },
        (error) => {
          this.setState({
            isLoaded: true,
            error,
          });
        }
      );
    }
  }

  renderCard = (shop) => {
    const {t} = this.props;

    return (
      <div className="card text-center">
        <div className="crd-body text-dark" id={shop.id}>
          <h2 className="card-title">{shop.name}</h2>
          <p>
            {t("Phone")}: {shop.phoneNumber}
          </p>
          <p>
            {t("Email")}: <br/> {shop.email}
          </p>
          <Button
            text={t("Edit")}
            onClick={(e) => {
              localStorage.setItem("Email", shop.email);
              localStorage.setItem("Role", "USER");
              window.location.href = "./edit";
            }}
          />
          <Button
            text={t("Delete")}
            onClick={() => this.deleteCompany(shop.email)}
          />
        </div>
      </div>
    );
  };
}

export default withTranslation()(Card);