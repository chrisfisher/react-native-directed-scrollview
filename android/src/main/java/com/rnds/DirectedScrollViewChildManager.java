package com.rnds;

import android.support.annotation.Nullable;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class DirectedScrollViewChildManager extends ViewGroupManager<DirectedScrollViewChild> {

  @Override
  public String getName() {
    return "DirectedScrollViewChild";
  }

  @Override
  public DirectedScrollViewChild createViewInstance(ThemedReactContext context) {
    return new DirectedScrollViewChild(context);
  }

  @ReactProp(name = "scrollDirection", customType = "none")
  public void setScrollDirection (DirectedScrollViewChild view, @Nullable String scrollDirection) {
    view.setScrollDirection(scrollDirection);
  }
}
