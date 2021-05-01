import React from "react";
import Button from "../../Interface/Button";
import { withTranslation } from "react-i18next";
import jwt_decode from "jwt-decode";
import "react-loader-spinner/dist/loader/css/react-spinner-loader.css";
import Loader from "react-loader-spinner";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

const url = "http://localhost:8080";

class RoomCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      rooms: []
    };
  }

  componentDidMount() {
    const res = `${url}/florist-shops/${decoded.email}/rooms`;
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
            rooms: result,
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
    const { t } = this.props;
    const { error, isLoaded, rooms } = this.state;
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
      return <div className="grid">{rooms.sort((a, b) => {
        return (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0)
      }).map(this.renderCard)}</div>;
    }
  }

  renderCard = (room) => {
    const { t } = this.props;

    if (decoded.role === "USER") {
      return (
        <div className="card text-center">
          <div className="crd-body text-dark" id={room.id}>
            <h2 className="card-title"> {t("Room")}: {room.id}</h2>
            <p className="card-text text-secondary">
              {t("FCity")}: {room.city}
            </p>
            <p className="card-text text-secondary">
              {t("FStreet")}: {room.street}
            </p>
            <p className="card-text text-secondary">
              {t("FHouse")}: {room.house}
            </p>
            <p className="card-text text-secondary">
              {t("Fullness")}: {room.actualCapacity}/{room.maxCapacity}
            </p>
            <hr/>
            <p className="card-text text-secondary">
              {t("Temp")}: {room.smartDevice.temperature} &deg;C
            </p>
            <p className="card-text text-secondary">
              {t("Hum")}: {room.smartDevice.humidity} %
            </p>
            <Button
              text={t("More")}
              onClick={() => {
                localStorage.setItem("RId", room.id);
                localStorage.setItem("actualCapacity", room.actualCapacity)
                window.location.href = "./more";
              }}
            />
            <Button
              text={t("Edit")}
              onClick={(e) => {
                localStorage.setItem("RId", room.id);
                window.location.href = "./edit_room";
              }}
            />
            <Button
              text={t("Delete")}
              onClick={() => this.deleteRoom(room.id)}
            />
          </div>
        </div>
      );
    }
  };

  deleteRoom(id) {
    const {t} = this.props;
    if (window.confirm(t("areYouSure"))) {
      fetch(`${url}/florist-shops/rooms/${id}`, {
        method: "delete",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("Token"),
        },
      }).then(
        (result) => {
          this.setState({
            rooms: this.state.rooms.filter(room => {
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
}

export default withTranslation()(RoomCard);
