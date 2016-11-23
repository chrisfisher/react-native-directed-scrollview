import React, { Component } from 'react';
import { View, requireNativeComponent } from 'react-native';

const NativeDirectedScrollView = requireNativeComponent('DirectedScrollView');

export default class DirectedScrollView extends Component {
  render() {
    return (
      <NativeDirectedScrollView {...this.props}>
        <View style={this.props.contentContainerStyle}>
          {this.props.children}
        </View>
      </NativeDirectedScrollView>
    );
  }
}