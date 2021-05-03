import React from "react";
import Button from "../Interface/Button";
import {withTranslation} from "react-i18next";
import {confirmAlert} from "react-confirm-alert";
import Loader from "react-loader-spinner";

var url = "http://localhost:8080";

class Card extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      floristShops: [],
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
    const {error, floristShops} = this.state;
    if (error) {
      return (
        <div className="additional">
          {t("Failiture")}: {error.message}
        </div>
      );
    } else if (!this.state.isLoaded) {
      return <div className="centered">
        <Loader
          type="BallTriangle"
          color="seagreen"
          height={425}
          width={425}
          timeout={10000}
        />
      </div>;
    } else {
      return <div className="grid">{floristShops.map(this.renderCard)}</div>;
    }
  }

  renderCard = (shop) => {
    const {t} = this.props;

    return (
      <div className="card text-center">
        <div className="crd-body text-dark" id={shop.id}>
          <h2 className="card-title">{shop.name}</h2>
          {
            shop.isLocked
              ? <p className="text-danger">{t("accIsBlocked")}</p>
              : <p className="text-success">{t("accIsActive")}</p>
          }
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
            className="btn btn-danger-warning"
            text={shop.isLocked ? t("UnlockUser") : t("LockUser")}
            onClick={() => this.lockUser(shop.email)}
          />
          <Button
            className="btn btn-danger"
            text={t("Delete")}
            onClick={() => this.submitDelete(shop.email)}
          />
        </div>
      </div>
    );
  };

  submitDelete = (shopEmail) => {
    const {t} = this.props;

    confirmAlert({
      title: t("Delete"),
      message: t("areYouSure"),
      buttons: [
        {
          label: t("yes"),
          onClick: () => this.deleteShop(shopEmail)
        },
        {
          label: t("no")
        }
      ],
      closeOnEscape: true,
      closeOnClickOutside: true,
    });
  };

  deleteShop(email) {
    const {t} = this.props;

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

  lockUser(email) {
    this.setState({isLoaded: false})
    fetch(`${url}/admin/lock-user/${email}`, {
      method: "post",
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

export default withTranslation()(Card);
