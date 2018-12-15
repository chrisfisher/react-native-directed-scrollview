import React, { Component } from "react";
import { AppRegistry, StyleSheet, SafeAreaView } from "react-native";

import Grid from "./src/components/Grid";
import { name as appName } from "./app.json";

class App extends Component {
  render() {
    return (
      <SafeAreaView style={styles.container}>
        <Grid />
      </SafeAreaView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1
  }
});

AppRegistry.registerComponent(appName, () => App);
