# react-native-directed-scrollview

![demo](example/rnds-demo.gif)

A natively implemented scrollview component which lets you specify different scroll directions for child content.

The iOS implementation extends the default UIScrollView component, whereas the Android implementation is custom and aims to provide some limited parity with the iOS api.

The following props are supported in both iOS and Android:

| Prop | Description |
| --- | --- |
| `minimumZoomScale` | How far the content can zoom out |
| `maximumZoomScale` | How far the content can zoom in |
| `bounces` | Whether content bounces at the limits when scrolling |
| `bouncesZoom` | Whether content bounces at the limits when zooming |

The following props are currently iOS only:

| Prop | Description |
| --- | --- |
| `showsVerticalScrollIndicator` | Whether vertical scroll bars are visible |
| `showsHorizontalScrollIndicator` | Whether horizontal scroll bars are visible |

## Installation

- `npm install react-native-directed-scrollview --save`
- `react-native link` (or `rnpm link`)

## Usage

To work properly this component requires that a fixed-size content container be specified through the contentContainerStyle prop.

```javascript
import ScrollView, { ScrollViewChild } from 'react-native-directed-scrollview';
...

export default class Example extends Component {
  render() {
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
          // multi-directional scrolling content here...      
        </ScrollViewChild>
        <ScrollViewChild scrollDirection={'vertical'}>
          // vertically scrolling content here...      
        </ScrollViewChild>
        <ScrollViewChild scrollDirection={'horizontal'}>
          // horizontally scrolling content here...      
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
    height: 1000,
    width: 1000,
  },
})
```

See the [example project](https://github.com/chrisfisher/react-native-directed-scrollview/tree/master/example) for more detail.
