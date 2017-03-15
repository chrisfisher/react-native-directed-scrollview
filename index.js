/**
 * @providesModule react-native-directed-scrollview
 */

import React, { Component } from 'react';
import ReactNative, { requireNativeComponent, View, UIManager, StyleSheet, Platform } from 'react-native';

const NativeScrollView = requireNativeComponent('DirectedScrollView');
const NativeScrollViewChild = requireNativeComponent('DirectedScrollViewChild');

const ScrollView = React.createClass({
  getScrollableNode: function(): any {
    return ReactNative.findNodeHandle(this._scrollViewRef);
  },
  scrollTo: function({ x, y, animated }) {
     UIManager.dispatchViewManagerCommand(
      this.getScrollableNode(),
      UIManager.DirectedScrollView.Commands.scrollTo,
      [x || 0, y || 0, animated !== false],
    );
  },
  zoomToFit: function({ animated }) {
     UIManager.dispatchViewManagerCommand(
      this.getScrollableNode(),
      UIManager.DirectedScrollView.Commands.zoomToFit,
      [animated !== false],
    );
  },
  _scrollViewRef: null,
  _setScrollViewRef: function(ref) {
    this._scrollViewRef = ref;
  },
  render: function() {
    return (
      <NativeScrollView {...this.props} ref={this._setScrollViewRef}>
        <View style={this.props.contentContainerStyle} pointerEvents={'box-none'}>
          {this.props.children}
        </View>
      </NativeScrollView>
    );
  }
});

export default ScrollView;

export const ScrollViewChild = React.createClass({
  render: function() {
    return (
      <NativeScrollViewChild {...this.props}>
        {this.props.children}
      </NativeScrollViewChild>
    );
  }
});

export const scrollViewWillBeginDragging = 'scrollViewWillBeginDragging';

export const scrollViewDidEndDragging = 'scrollViewDidEndDragging';
