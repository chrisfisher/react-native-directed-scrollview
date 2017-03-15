//
//  DirectedScrollViewChildManager.m
//  DirectedScrollViewChildManager
//

#import "DirectedScrollViewChildManager.h"
#import <React/RCTScrollView.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>

@implementation DirectedScrollViewChild

static NSString *const SCROLL_DIRECTION_BOTH = @"both";
static NSString *const SCROLL_DIRECTION_HORIZONTAL = @"horizontal";
static NSString *const SCROLL_DIRECTION_VERTICAL = @"vertical";

- (BOOL)shouldScrollHorizontally {
  return [self.scrollDirection isEqualToString:SCROLL_DIRECTION_BOTH] ||
    [self.scrollDirection isEqualToString:SCROLL_DIRECTION_HORIZONTAL];
}

- (BOOL)shouldScrollVertically {
  return [self.scrollDirection isEqualToString:SCROLL_DIRECTION_BOTH] ||
    [self.scrollDirection isEqualToString:SCROLL_DIRECTION_VERTICAL];
}

@end

@implementation DirectedScrollViewChildManager

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (UIView *)view
{
    DirectedScrollViewChild *directedScrollViewChild = [[DirectedScrollViewChild alloc] init];

    directedScrollViewChild.pointerEvents = RCTPointerEventsBoxNone;

    return directedScrollViewChild;
}

RCT_EXPORT_VIEW_PROPERTY(scrollDirection, NSString)

@end
