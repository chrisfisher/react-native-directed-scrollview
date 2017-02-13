/* @flow */

import React, { Component } from 'react';
import { Text, View, StyleSheet } from 'react-native';
import type { Row, Cell } from '../data';
import colors from '../colors';

export default class GridContent extends Component {
  props: {
    cellsByRow: Array<Row>
  }

  render() {
    return (
      <View>
        { this.props.cellsByRow.map(row => this._renderRow(row)) }
      </View>
    );
  }

  _renderRow(row: Row) {
    return (
      <View key={row.id} style={styles.rowContainer}>
        { row.cells.map(cell => this._renderCell(cell)) }
      </View>
    );
  }

  _renderCell(cell: Cell) {
    return (
      <View key={cell.id} style={styles.cellContainer}>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  rowContainer: {
    flexDirection: 'row',
  },
  cellContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    height: 100,
    width: 100,
    margin: 10,
    backgroundColor: colors.lightGray,
  },
});
