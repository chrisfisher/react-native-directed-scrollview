/* @flow */

import React, { Component } from 'react'
import { Text, View, StyleSheet } from 'react-native'
import type { Row, Cell } from '../data'
import colors from '../colors'

export default class RowLabels extends Component {
  render() {
    return (
      <View style={styles.container}>
        { this.props.cellsByRow.map(row => this._renderRowLabel(row)) }
      </View>
    )
  }

  _renderRowLabel(row: Row) {
    return (
      <View key={row.id} style={styles.rowLabel}>
        <Text style={styles.rowTitle}>
          Row label
        </Text>
      </View>
    )
  }
}

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: 0,
    left: 0,
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  rowLabel: {
    height: 120,
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  rowTitle: {
    backgroundColor: colors.darkPurple,
    paddingVertical: 4,
    paddingHorizontal: 10,
    color: colors.white,
    fontWeight: '500',
    fontSize: 16,
  },
})