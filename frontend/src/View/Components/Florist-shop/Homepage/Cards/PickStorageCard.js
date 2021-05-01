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
          timeout={10000}
        />
      </div>;
    } else if (storages.length === 0) {
      return <div className="centered">
        <h1>{t("NoStorages")}</h1>
      </div>
    } else {
      const storage = storages[0]

      return <div>
        <div className="rooms_back">
          <p>
            {t("RoomNumber")} {localStorage.getItem('RId')} &#124;
            &nbsp;{t("City")} {storage.city}, {t("Street")} {storage.street}, {t("House")} {storage.house} &#124;
            &nbsp;{t("Fullness")} {localStorage.getItem('actualCapacity')}/{storage.maxCapacity} &#124;
            &nbsp;{t("Temp")}: {storage.temperature}&deg;C
            / {t("Hum")}: {storage.humidity}%
          </p>
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
          label: 'Yes',
          onClick: () => this.deleteStorage(storageId)
        },
        {
          label: 'No'
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

}

export default withTranslation()(PickStorageCard);
