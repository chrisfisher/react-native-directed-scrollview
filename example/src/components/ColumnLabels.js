/* @flow */

import React, { Component } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import type { Cell } from '../data';
import colors from '../colors';

export default class ColumnLabels extends Component {
  render() {
    return (
      <View style={styles.container} pointerEvents={'box-none'}>
        { this.props.cellsByRow[1].cells.map((cell, index) => this._renderColumnLabel(cell, index)) }
      </View>
    );
  }

  _renderColumnLabel(cell: Cell, index: number) {
    return (
      <View key={cell.id} style={styles.columnLabel} pointerEvents={'box-none'}>
        <Text style={styles.columnTitle}>
          {index + 1}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row'
  },
  columnLabel: {
    width: 120,
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
