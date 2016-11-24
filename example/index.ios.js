import React, { Component } from 'react'
import { AppRegistry, StyleSheet, View } from 'react-native'
import Grid from './src/components/Grid'

export default class example extends Component {
  render() {
    return (
      <View style={styles.container}>
        <Grid />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    marginTop: 20,
  },
})

AppRegistry.registerComponent('example', () => example)
