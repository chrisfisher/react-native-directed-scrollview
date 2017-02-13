package com.rnds;

import android.support.annotation.Nullable;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

class DirectedScrollViewManager extends ViewGroupManager<DirectedScrollView> {

  @Override
  public String getName() {
    return "DirectedScrollView";
  }

  @Override
  public DirectedScrollView createViewInstance(ThemedReactContext context) {
    return new DirectedScrollView(context);
  }

  @ReactProp(name = "minimumZoomScale", defaultFloat = 1.0f)
  public void setMinimumZoomScale(DirectedScrollView view, @Nullable float minimumZoomScale) {
    view.setMinimumZoomScale(minimumZoomScale);
  }

  @ReactProp(name = "maximumZoomScale", defaultFloat = 1.0f)
  public void setMaximumZoomScale(DirectedScrollView view, @Nullable float maximumZoomScale) {
    view.setMaximumZoomScale(maximumZoomScale);
  }

  @ReactProp(name = "bounces", defaultBoolean = true)
  public void setBounces(DirectedScrollView view, @Nullable boolean bounces) {
    view.setBounces(bounces);
  }

  @ReactProp(name = "bouncesZoom", defaultBoolean = true)
  public void setBouncesZoom(DirectedScrollView view, @Nullable boolean bouncesZoom) {
    view.setBouncesZoom(bouncesZoom);
  }
}
