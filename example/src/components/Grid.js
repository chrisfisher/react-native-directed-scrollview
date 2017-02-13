/* @flow */

import React, { Component } from 'react';
import { StyleSheet, View } from 'react-native';
import ScrollView, { ScrollViewChild } from 'react-native-directed-scrollview';
import GridContent from './GridContent';
import RowLabels from './RowLabels';
import ColumnLabels from './ColumnLabels';
import { getCellsByRow } from '../data';

export default class Grid extends Component {
  render() {
    const cellsByRow = getCellsByRow()

    return (
      <ScrollView
        bounces={true}
        bouncesZoom={true}
        maximumZoomScale={1.5}
        minimumZoomScale={0.75}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.contentContainer}
        style={styles.container}
      >
        <ScrollViewChild scrollDirection={'both'}>
          <GridContent cellsByRow={cellsByRow} />
        </ScrollViewChild>
        <ScrollViewChild scrollDirection={'vertical'}>
          <RowLabels cellsByRow={cellsByRow} />
        </ScrollViewChild>
        <ScrollViewChild scrollDirection={'horizontal'}>
          <ColumnLabels cellsByRow={cellsByRow} />
        </ScrollViewChild>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  contentContainer: {
    height: 1080,
    width: 1080,
  },
});
