/* @flow */

import React, { Component } from 'react'
import { StyleSheet, View } from 'react-native'
import DirectedScrollView from 'react-native-directed-scrollview'
import GridContent from './GridContent'
import RowLabels from './RowLabels'
import ColumnLabels from './ColumnLabels'
import { getCellsByRow } from '../data'

export default class Grid extends Component {
  render() {
    const cellsByRow = getCellsByRow()

    return (
      <DirectedScrollView
        bounces={true}
        maximumZoomScale={2}
        minimumZoomScale={0.5}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}
        contentContainerStyle={styles.contentContainer}
        horizontallyScrollingSubviewIndex={1}
        verticallyScrollingSubviewIndex={2}
      >
        <GridContent cellsByRow={cellsByRow} />
        <ColumnLabels cellsByRow={cellsByRow} />        
        <RowLabels cellsByRow={cellsByRow} />        
      </DirectedScrollView>
    );
  }
}

const styles = StyleSheet.create({
  contentContainer: {
    height: 1080,
    width: 1080,
  },
})
