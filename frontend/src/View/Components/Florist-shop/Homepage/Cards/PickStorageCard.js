import React from "react";
import Button from "../../../Interface/Button";
import {withTranslation} from "react-i18next";
import jwt_decode from "jwt-decode";
import Loader from "react-loader-spinner";
import {confirmAlert} from "react-confirm-alert";

if (localStorage.getItem("Token") != null) {
  var token = localStorage.getItem("Token");
  var decoded = jwt_decode(token);
}

const url = "http://localhost:8080";

class PickStorageCard extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      storages: []
    };

    Date.prototype.addDays = function (days) {
      var date = new Date(this.valueOf());
      date.setDate(date.getDate() + days);
      return date;
    }
  }

  componentDidMount() {
    const res = `${url}/flower-storages/storage-room/${localStorage.getItem('RId')}`;
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
          this.setState({
            isLoaded: true,
            storages: result,
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
    const {error, isLoaded, storages} = this.state;
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
          height={400}
          width={400}
          timeout={30000}
        />
      </div>;
    } else if (storages.length === 0) {
      return <div>
        <h1 className="centered-no-storages">{t("NoStorages")}</h1>
        <Button
          text={t("AddStorage")}
          onClick={(e) => {
            window.location.href = "./add_storage";
          }}
        /></div>
    } else {
      const storage = storages[0]

      return <div>
        <div className="rooms_back">
          <p>
            {t("RoomNumber")} {localStorage.getItem('RId')} &#124;
            &nbsp;{t("City")} {storage.city}, {t("Street")} {storage.street}, {t("House")} {storage.house} &#124;
            &nbsp;{t("Fullness")} {storage.actualCapacity}/{storage.maxCapacity} &#124;
            &nbsp;{t("Temp")}: {storage.temperature}&deg;C
            / {t("Hum")}: {storage.humidity}%
          </p>
          <Button
            text={t("redistribution")}
            onClick={(e) => {
              this.submitRedistribution(storage)
            }}
          />
          <Button
            text={t("AddStorage")}
            onClick={(e) => {
              localStorage.setItem("RId", storage.storageRoomId);
              localStorage.setItem("Address", storage.city + ', ' + storage.street + ' ' + storage.house)
              window.location.href = "./add_storage";
            }}
          />
        </div>
        <div className="grid">{storages.sort((a, b) => {
          return (a.formattedDate > b.formattedDate) ? -1 : ((b.formattedDate > a.formattedDate) ? 1 : 0)
        }).map(this.renderCard)}</div>
      </div>;
    }
  }

  renderCard = (storage) => {
    const {t} = this.props;

    const startDate = new Date(Date.parse(storage.startDate));
    const lastStorageDate = Date.parse(startDate.addDays(Number.parseInt(storage.flowerShelfLife)).toDateString())
    const actualDate = Date.parse(new Date().toDateString())

    return (
      <div className="card text-center">
        <div className="crd-body text-dark" id={storage.id}>
          <h2 className="card-title">{storage.flowerName}</h2>
          <hr/>
          {actualDate > lastStorageDate ?
            <p className="card-text text-secondary text-danger">
              {t("ExpiredShelfLife")}
            </p> :
            <p className="card-text text-secondary text-success">
              {t("NormalShelfLife")}
            </p>
          }
          {(storage.temperature < storage.minTemperature ||
            storage.temperature > storage.maxTemperature) ?
            <p className="card-text text-secondary text-danger">
              {t("AbnormalClimate")}
            </p> :
            <p className="card-text text-secondary text-success">
              {t("NormalClimate")}
            </p>
          }
          <p className="card-text text-secondary">
            {t("FColor")}: {storage.flowerColor}
          </p>
          <p className="card-text text-secondary">
            {t("StartDate")}: {storage.formattedDate}
          </p>
          <p className="card-text text-secondary">
            {t("FShelfLife")}: {storage.flowerShelfLife}
          </p>
          <p className="card-text text-secondary">
            {t("Amount")}: {storage.amount}
          </p>
          <p className="card-text text-secondary">
            {t("TempInterval")}: {storage.minTemperature}-{storage.maxTemperature}&deg;C
          </p>
          <Button
            text={t("Edit")}
            onClick={(e) => {
              localStorage.setItem("SId", storage.id);
              window.location.href = "./edit_storage";
            }}
          />
          <Button
            text={t("Delete")}
            onClick={() => this.submitDelete(storage.id)}
          />
        </div>
      </div>
    );
  }

  submitDelete = (storageId) => {
    const {t} = this.props;

    confirmAlert({
      title: t("Delete"),
      message: t("areYouSure"),
      buttons: [
        {
          label: t("yes"),
          onClick: () => this.deleteStorage(storageId)
        },
        {
          label: t("no")
        }
      ],
      closeOnEscape: true,
      closeOnClickOutside: true,
    });
  };

  deleteStorage(id) {
    const {t} = this.props;
    fetch(`${url}/flower-storages/${id}`, {
      method: "delete",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("Token"),
      },
    }).then(
      (result) => {
        this.setState({
          storages: this.state.storages.filter(storage => {
              if (storage.id === id) {
                let actualCapacity = localStorage.getItem('actualCapacity')
                localStorage.setItem('actualCapacity', (actualCapacity - storage.amount).toString())
              }
              return storage.id !== id
            }
          )
        });
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

  submitRedistribution = (storage) => {
    const {t} = this.props;

    confirmAlert({
      title: t("redistribution"),
      message: t("areYouSureRedistribute"),
      buttons: [
        {
          label: t("yes"),
          onClick: () => this.redistribute(storage)
        },
        {
          label: t("no")
        }
      ],
      closeOnEscape: true,
      closeOnClickOutside: true
    });
  };

  redistribute(storage) {
    const {t} = this.props;
    this.setState({isLoaded: false})
    fetch(`${url}/device`, {
      method: "post",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("Token"),
      },
      body: JSON.stringify({
        id: storage.storageRoomId,
        airQuality: storage.airQuality,
        humidity: storage.humidity,
        temperature: storage.temperature,
        satisfactionFactor: storage.satisfactionFactor
      })
    }).then((res) => res.json())
      .then(result => {
        console.log(result)
          confirmAlert({
            title: t("redistribution"),
            message: this.createRedistributionMessage(result),
            buttons: [
              {
                label: "Ok",
                onClick: () => window.location.reload()
              }
            ],
            closeOnEscape: false,
            closeOnClickOutside: false
          });
        },
        (error) => {
          console.log(error)
        }
      );
  }

  createRedistributionMessage(json) {
    const {t} = this.props;

    let resultMessage = ""
    let flower, room
    json.forEach(storage => {
      flower = storage.flower
      room = storage.storageRoom
      resultMessage += `${t("flower")} ${flower.name} (${flower.color}) ${t("inCount")} ${storage.amount}
      ${t("movedTo")} ${room.id} (${room.city}, ${room.street} ${room.house}).\r\n`
    })
    if (resultMessage === "") {
      resultMessage = t("noRedistributionPerformed")
    }
    return resultMessage
  }

}

export default withTranslation()(PickStorageCard);
