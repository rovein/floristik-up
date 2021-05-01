import React from "react";
import Button from "../../../Interface/Button";
import {withTranslation} from "react-i18next";
import jwt_decode from "jwt-decode";
import Loader from "react-loader-spinner";
import {confirmAlert} from 'react-confirm-alert'; // Import
import 'react-confirm-alert/src/react-confirm-alert.css'; //

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

const url = "http://localhost:8080";

class PickFlowerCard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      flowers: []
    };
  }

  componentDidMount() {
    const res = `${url}/flowers`;
    fetch(res, {
      method: "get",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
      .then((res) => res.json())
      .then(
        (result) => {
          console.log(result)
          this.setState({
            isLoaded: true,
            flowers: result,
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
    const {error, isLoaded, flowers} = this.state;
    if (error) {
      return (
        <div className="additional">
          {t("Failiture")}: {error.message}
        </div>
      );
    } else if (!isLoaded) {
      return <div className="centered">
        <Loader
          type="BallTriangle"
          color="seagreen"
          height={350}
          width={350}
          timeout={10000}
        />
      </div>;
    } else {
      return <div className="grid">{flowers.map(this.renderCard)}</div>;
    }
  }

  renderCard = (flower) => {
    const {t} = this.props;

    if (decoded.role === "USER") {
      return (
        <div className="card text-center">
          <div className="crd-body text-dark" id={flower.id}>
            <h2 className="card-title">{flower.name}</h2>
            <hr/>
            <p className="card-text text-secondary">
              {t("FColor")}: {flower.color}
            </p>
            <p className="card-text text-secondary">
              {t("FShelfLife")}: {flower.shelfLife}
            </p>
            <p className="card-text text-secondary">
              {t("FMinTemp")}: {flower.minTemperature} &deg;C
            </p>
            <p className="card-text text-secondary">
              {t("FMaxTemp")}: {flower.maxTemperature} &deg;C
            </p>
            <Button
              text={t("Edit")}
              onClick={(e) => {
                localStorage.setItem("FId", flower.id);
                window.location.href = "./edit_flower";
              }}
            />
            <Button
              text={t("Delete")}
              onClick={() => this.submitDelete(flower.id)}
            />
          </div>
        </div>
      );
    }
  };

  submitDelete = (flowerId) => {
    const {t} = this.props;

    confirmAlert({
      title: t("Delete"),
      message: t("areYouSure"),
      buttons: [
        {
          label: t("yes"),
          onClick: () => this.deleteFlower(flowerId)
        },
        {
          label: t("no")
        }
      ],
      closeOnEscape: true,
      closeOnClickOutside: true,
    });
  };

  deleteFlower(id) {
    const {t} = this.props;
    fetch(`${url}/flowers/${id}`, {
      method: "delete",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("Token"),
      },
    }).then(
      (result) => {
        this.setState({
          flowers: this.state.flowers.filter(room => {
              return room.id !== id
            }
          )
        })
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

export default withTranslation()(PickFlowerCard);
