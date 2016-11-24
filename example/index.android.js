import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

export default class example extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.text}>
          Not yet implemented...
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  text: {
    fontSize: 16,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('example', () => example);
