package com.rnds;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ScaleGestureDetector;
import android.view.animation.Interpolator;

import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.NativeGestureUtil;
import com.facebook.react.views.scroll.ReactScrollViewHelper;
import com.facebook.react.views.view.ReactViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DirectedScrollView extends ReactViewGroup {

  private static final long SNAP_BACK_ANIMATION_DURATION = 120;
  private static final Interpolator SNAP_BACK_ANIMATION_INTERPOLATOR = new FastOutLinearInInterpolator();

  private float minimumZoomScale = 1.0f;
  private float maximumZoomScale = 1.0f;
  private boolean bounces = true;
  private boolean bouncesZoom = true;

  private float pivotX;
  private float pivotY;
  private float scrollX;
  private float scrollY;
  private float startScrollX;
  private float startScrollY;
  private float startTouchX;
  private float startTouchY;
  private float scaleFactor = 1.0f;
  private boolean isScaleInProgress;
  private boolean isScrollInProgress;

  private ScaleGestureDetector scaleDetector;

  public DirectedScrollView(Context context) {
    super(context);

    initGestureListeners(context);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();

    anchorChildren();
  }

  @Override
  public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
    ReactScrollViewHelper.emitScrollBeginDragEvent(this);
    
    return true;
  }

  private void initGestureListeners(Context context) {

    setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
          case MotionEvent.ACTION_DOWN:
            onActionDown(motionEvent);
            break;
          case MotionEvent.ACTION_POINTER_DOWN:
            onActionPointerDown();
            break;
          case MotionEvent.ACTION_MOVE:
            onActionMove(motionEvent);
            break;
          case MotionEvent.ACTION_UP:
            onActionUp();
            break;
        }

        scaleDetector.onTouchEvent(motionEvent);

        return true;
      }
    });

    scaleDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.SimpleOnScaleGestureListener() {

      @Override
      public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();

        pivotX = detector.getFocusX();
        pivotY = detector.getFocusY();

        if (bouncesZoom) {
          scaleChildren(false);
        } else {
          clampAndScaleChildren(false);
          clampAndTranslateChildren(false);
        }

        invalidate();

        return true;
      }
    });
  }

  private void onActionDown(MotionEvent motionEvent) {
    startTouchX = motionEvent.getX();
    startTouchY = motionEvent.getY();
    startScrollX = scrollX;
    startScrollY = scrollY;
  }

  private void onActionPointerDown() {
    isScaleInProgress = true;
  }

  private void onActionMove(MotionEvent motionEvent) {
    NativeGestureUtil.notifyNativeGestureStarted(this, motionEvent);
    
    if (isScaleInProgress) return;

    isScrollInProgress = true;

    float deltaX = motionEvent.getX() - startTouchX;
    float deltaY = motionEvent.getY() - startTouchY;

    scrollX = startScrollX + deltaX;
    scrollY = startScrollY + deltaY;

    if (bounces) {
      translateChildren(false);
    } else {
      clampAndTranslateChildren(false);
    }

    ReactScrollViewHelper.emitScrollEvent(this);
  }

  private void onActionUp() {
    if (isScrollInProgress) {
      ReactScrollViewHelper.emitScrollEndDragEvent(this);
      isScrollInProgress = false;
    }

    if (bounces) {
      clampAndTranslateChildren(true);
    }

    if (bouncesZoom) {
      clampAndScaleChildren(true);
    }

    isScaleInProgress = false;
  }

  private void clampAndTranslateChildren(boolean animated) {
    if (getMaxScrollX() > 0) {
      scrollX = clamp(scrollX, -getMaxScrollX(), 0);
    } else {
      scrollX = 0;
    }

    if (getMaxScrollY() > 0) {
      scrollY = clamp(scrollY, -getMaxScrollY(), 0);
    } else {
      scrollY = 0;
    }

    translateChildren(animated);
  }

  private void clampAndScaleChildren(boolean animated) {
    scaleFactor = clamp(scaleFactor, minimumZoomScale, maximumZoomScale);

    scaleChildren(animated);
  }

  private void scaleChildren(boolean animated) {
    List<DirectedScrollViewChild> scrollableChildren = getScrollableChildren();

    for (DirectedScrollViewChild scrollableChild : scrollableChildren) {
      if (scrollableChild.getShouldScrollHorizontally())
        scrollableChild.setPivotX(pivotX - scrollX);
      if (scrollableChild.getShouldScrollVertically())
        scrollableChild.setPivotY(pivotY - scrollY);
      if (animated) {
        animateProperty(scrollableChild, "scaleX", scrollableChild.getScaleX(), scaleFactor);
        animateProperty(scrollableChild, "scaleY", scrollableChild.getScaleY(), scaleFactor);
      } else {
        scrollableChild.setScaleX(scaleFactor);
        scrollableChild.setScaleY(scaleFactor);
      }
    }
  }

  private void translateChildren(boolean animated) {
    List<DirectedScrollViewChild> scrollableChildren = getScrollableChildren();

    for (DirectedScrollViewChild scrollableChild : scrollableChildren) {
      if (scrollableChild.getShouldScrollHorizontally()) {
        if (animated) {
          animateProperty(scrollableChild, "translationX", scrollableChild.getTranslationX(), scrollX);
        } else {
          scrollableChild.setTranslationX(scrollX);
        }
      }

      if (scrollableChild.getShouldScrollVertically()) {
        if (animated) {
          animateProperty(scrollableChild, "translationY", scrollableChild.getTranslationY(), scrollY);
        } else {
          scrollableChild.setTranslationY(scrollY);
        }
      }
    }
  }

  private void anchorChildren() {
    List<DirectedScrollViewChild> scrollableChildren = getScrollableChildren();

    for (DirectedScrollViewChild scrollableChild : scrollableChildren) {
      scrollableChild.setPivotY(0);
      scrollableChild.setPivotX(0);
    }
  }

  private void animateProperty(Object target, String property, float start, float end) {
    if (start == end) return;

    ObjectAnimator anim = ObjectAnimator.ofFloat(target, property, start, end);
    anim.setDuration(SNAP_BACK_ANIMATION_DURATION);
    anim.setInterpolator(SNAP_BACK_ANIMATION_INTERPOLATOR);
    anim.start();
  }

  private float clamp(float value, float min, float max) {
    return Math.max(min, Math.min(value, max));
  }

  private float getContentContainerWidth() {
    return getChildAt(0).getWidth() * scaleFactor;
  }

  private float getContentContainerHeight() {
    return getChildAt(0).getHeight() * scaleFactor;
  }

  private float getMaxScrollX() {
    return getContentContainerWidth() - getWidth();
  }

  private float getMaxScrollY() {
    return getContentContainerHeight() - getHeight();
  }

  private ArrayList<DirectedScrollViewChild> getScrollableChildren() {
    ArrayList<DirectedScrollViewChild> scrollableChildren = new ArrayList<>();

    for (int i = 0; i < getChildCount(); i++) {
      View childView = getChildAt(i);

      if (childView instanceof DirectedScrollViewChild) {
        scrollableChildren.add((DirectedScrollViewChild) childView);
      }
    }

    return scrollableChildren;
  }

  public void setMaximumZoomScale(final float maximumZoomScale) {
    this.maximumZoomScale = maximumZoomScale;
  }

  public void setMinimumZoomScale(final float minimumZoomScale) {
    this.minimumZoomScale = minimumZoomScale;
  }

  public void setBounces(final boolean bounces) {
    this.bounces = bounces;
  }

  public void setBouncesZoom(final boolean bouncesZoom) {
    this.bouncesZoom = bouncesZoom;
  }

  public void scrollTo(Double x, Double y, Boolean animated) {
    float convertedX = PixelUtil.toPixelFromDIP(x);
    float convertedY = PixelUtil.toPixelFromDIP(y);
    scrollX = -convertedX;
    scrollY = -convertedY;

    translateChildren(animated);
  }
}

