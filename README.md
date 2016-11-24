# react-native-directed-scrollview
A natively implemented scrollview component which allows some content to scroll in both directions and other content to only scroll horizontally or vertically.

This component extends the default ScrollView component but has a much more limited API. It does not broadcast an `onScroll` event to JavaScript, as the intention was to make the scroll direction of subviews configurable through a declarative API and avoid having to do frame-by-frame calculations in JavaScript for performance reasons.

The implementation follows the same approach that the default ScrollView component uses for sticky section headers.

Currently only working in iOS. Android implementation is work in progress.

## Installation

- `npm install react-native-directed-scrollview --save`
- `react-native link` (or `rnpm link`)

## Usage

To work properly this component requires that a fixed-size content container be specified through the contentContainerStyle prop.

```javascript
import DirectedScrollView from 'react-native-directed-scrollview'
...

export default class Example extends Component {
  render() {
    return (
      <DirectedScrollView
        horizontallyScrollingSubviewIndex={1}
        verticallyScrollingSubviewIndex={2}
        contentContainerStyle={styles.contentContainer}     
        bounces={true}
        maximumZoomScale={2}
        minimumZoomScale={0.5}
        showsHorizontalScrollIndicator={false}
        showsVerticalScrollIndicator={false}
      >
        <MultiDirectionalScrollingContent />        
        <HorizontallyScrollingContent />  
        <VerticallyScrollingContent />  
      </DirectedScrollView>
    );
  }
}

const styles = StyleSheet.create({
  contentContainer: {
    height: 1000,
    width: 1000,
  },
})
```

See the [example project](https://github.com/chrisfisher/react-native-directed-scrollview/tree/master/example) for more detail.