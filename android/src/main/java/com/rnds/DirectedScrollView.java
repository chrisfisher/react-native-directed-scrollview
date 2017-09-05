package com.rnds;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Matrix;
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
      public boolean onScaleBegin(ScaleGestureDetector detector) {
        if (isScrollInProgress) {
          return false;
        }

        float x = detector.getFocusX();
        float y = detector.getFocusY();
        pivotChildren(x, y);
        updateChildren();
        return true;
      }

      @Override
      public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        updateChildren();
        return true;
      }

      private void updateChildren() {
        if (bouncesZoom) {
          scaleChildren(false);
        } else {
          clampAndScaleChildren(false);
        }

        if (bounces) {
          translateChildren(false);
        } else {
          clampAndTranslateChildren(false);
        }
        invalidate();
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

    ReactScrollViewHelper.emitScrollEvent(this, 0, 0);
  }

  private void onActionUp() {
    if (isScrollInProgress) {
      ReactScrollViewHelper.emitScrollEndDragEvent(this, 0, 0);
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
    float[] minPoints = transformPoints(new float[] { 0, 0 });
    float minX = minPoints[0];
    float minY = minPoints[1];
    float maxX = minPoints[0] + getMaxScrollX();
    float maxY = minPoints[1] + getMaxScrollY();

    if (maxX > minX) {
      scrollX = clamp(scrollX, -maxX, -minX);
    } else {
      scrollX = -minX;
    }

    if (maxY > minY) {
      scrollY = clamp(scrollY, -maxY, -minY);
    } else {
      scrollY = -minY;
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

  private void pivotChildren(float newPivotX, float newPivotY) {
    float oldPivotX = pivotX;
    float oldPivotY = pivotY;
    pivotX = newPivotX - scrollX;
    pivotY = newPivotY - scrollY;

    scrollX += (oldPivotX - pivotX) * (1 - scaleFactor);
    scrollY += (oldPivotY - pivotY) * (1 - scaleFactor);

    List<DirectedScrollViewChild> scrollableChildren = getScrollableChildren();
    for (DirectedScrollViewChild scrollableChild : scrollableChildren) {
      if (scrollableChild.getShouldScrollHorizontally()) {
        scrollableChild.setTranslationX(scrollX);
        scrollableChild.setPivotX(pivotX);
      }
      if (scrollableChild.getShouldScrollVertically()) {
        scrollableChild.setTranslationY(scrollY);
        scrollableChild.setPivotY(pivotY);
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

  private float[] transformPoints(float[] points) {
    float[] transformedPoints = new float[points.length];

    Matrix matrix = new Matrix();
    matrix.setScale(scaleFactor, scaleFactor, pivotX, pivotY);
    matrix.mapPoints(transformedPoints, points);

    return transformedPoints;
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
