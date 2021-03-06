import React from "react";
import "./App.css";
import {observer} from "mobx-react";
import {Route, Switch, BrowserRouter, Redirect} from "react-router-dom";
import SignIn from "./View/Components/Navigation/Login";
import SignUp from "./View/Components/Navigation/SignUp";
import Profile from "./View/Components/Navigation/Profile";
import {withTranslation} from "react-i18next";
import Edit from "./View/Components/Navigation/Edit";

import AddRoom from "./View/Components/Navigation/Room/AddRoom";
import EditRoom from "./View/Components/Navigation/Room/EditRoom";

import MoreInfo from "./View/Components/Navigation/MoreInfo";
import AddFlower from "./View/Components/Navigation/Flower/AddFlower";
import EditFlower from "./View/Components/Navigation/Flower/EditFlower";
import AddStorage from "./View/Components/Navigation/Storage/AddStorage";
import EditStorage from "./View/Components/Navigation/Storage/EditStorage";
import CreateAccount from "./View/Components/Navigation/CreateAccount";

class App extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      value: "en",
    };
  }

  render() {
    return (
      <div className="App">
        <BrowserRouter>
          <Switch>
            <Route path="/login" component={SignIn}/>
            <Route path="/signup" component={SignUp}/>

            <Route path="/profile" component={Profile}/>
            <Route path="/edit" component={Edit}/>
            <Route path="/more" component={MoreInfo}/>

            <Route path="/add_room" component={AddRoom}/>
            <Route path="/edit_room" component={EditRoom}/>

            <Route path="/add_flower" component={AddFlower} />
            <Route path="/edit_flower" component={EditFlower} />

            <Route path="/add_storage" component={AddStorage} />
            <Route path="/edit_storage" component={EditStorage} />

            <Route path="/create-account" component={CreateAccount}/>

            <Redirect from="/" to="/login"/>
          </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default withTranslation()(observer(App));
