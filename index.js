import React, { Component } from 'react';
import ReactNative, { requireNativeComponent, View, UIManager } from 'react-native';

const NativeDirectedScrollView = requireNativeComponent('DirectedScrollView');

const DirectedScrollView = React.createClass({
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
  _scrollViewRef: null,
  _setScrollViewRef: function(ref) {
    this._scrollViewRef = ref;
  },
  render: function() {
    return (
      <NativeDirectedScrollView {...this.props} ref={this._setScrollViewRef}>
        <View style={this.props.contentContainerStyle}>
          {this.props.children}
        </View>
      </NativeDirectedScrollView>
    );
  }
});

export default DirectedScrollView;