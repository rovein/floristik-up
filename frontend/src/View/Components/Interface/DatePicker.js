import React, { useState } from "react";

import DateTimePicker from "react-datetime-picker";

class ChangeExample extends React.Component {
  constructor(...args) {
    super(...args);
    this.state = { value: this.props.initialValue };
  }
  render() {
    return (
      <DateTimePicker
        value={this.state.value}
        onChange={(value) => {
          this.setState({ value });
          localStorage.setItem("date", value);
        }}
      />
    );
  }
}

export default ChangeExample;
