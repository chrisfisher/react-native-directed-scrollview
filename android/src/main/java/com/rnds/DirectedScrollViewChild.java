package com.rnds;

import android.content.Context;

import com.facebook.common.internal.Objects;
import com.facebook.react.views.view.ReactViewGroup;

public class DirectedScrollViewChild extends ReactViewGroup {

  private static final String SCROLL_DIRECTION_BOTH = "both";
  private static final String SCROLL_DIRECTION_HORIZONTAL = "horizontal";
  private static final String SCROLL_DIRECTION_VERTICAL = "vertical";

  private String scrollDirection;

  public DirectedScrollViewChild(Context context) {
    super(context);
  }

  public boolean getShouldScrollHorizontally() {
    return Objects.equal(this.scrollDirection, SCROLL_DIRECTION_BOTH) || Objects.equal(this.scrollDirection, SCROLL_DIRECTION_HORIZONTAL);
  }

  public boolean getShouldScrollVertically() {
    return Objects.equal(this.scrollDirection, SCROLL_DIRECTION_BOTH) || Objects.equal(this.scrollDirection, SCROLL_DIRECTION_VERTICAL);
  }

  public void setScrollDirection(final String scrollDirection) {
    this.scrollDirection = scrollDirection;
  }
}
