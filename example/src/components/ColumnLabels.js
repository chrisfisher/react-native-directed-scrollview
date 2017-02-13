/* @flow */

import React, { Component } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import type { Cell } from '../data';
import colors from '../colors';

export default class ColumnLabels extends Component {
  render() {
    return (
      <View style={styles.container}>
        { this.props.cellsByRow[1].cells.map((cell, index) => this._renderColumnLabel(cell, index)) }
      </View>
    );
  }

  _renderColumnLabel(cell: Cell, index: number) {
    return (
      <View key={cell.id} style={styles.columnLabel}>
        <Text style={styles.columnTitle}>
          {index + 1}
        </Text>
      </View>
    );
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
    flexDirection: 'row'
  },
  columnLabel: {
    width: 120,
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  columnTitle: {
    backgroundColor: colors.lightGreen,
    paddingVertical: 4,
    paddingHorizontal: 10,
    color: colors.white,
    fontWeight: '500',
    fontSize: 16,
  },
});
