//
//  DirectedScrollViewManager.m
//  DirectedScrollViewManager
//

#import "DirectedScrollViewManager.h"
#import "DirectedScrollViewChildManager.h"
#import <React/RCTScrollView.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>

@interface DirectedScrollView : RCTScrollView

@property (nonatomic, weak) id <DirectedScrollViewDelegate> delegate;

@end

@implementation DirectedScrollView

#pragma mark - ScrollView delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    [super scrollViewDidScroll:scrollView];
    UIView *contentView = [self contentView];

    for (UIView *subview in contentView.reactSubviews)
    {
        DirectedScrollViewChild *scrollableChild = (DirectedScrollViewChild*)subview;

        if (subview == nil) continue;

        if (![scrollableChild shouldScrollVertically]) {
          CGFloat scrollTop = scrollView.contentOffset.y + self.contentInset.top;

          // adjust the y offset based on the current zoom scale
          // if we're zoomed in the offset required will be less, if we're zoomed out it will be more
          CGFloat yOffset = scrollTop / scrollView.zoomScale;

          // translate the horizontally scrolling subview by the calculated y offset
          // this cancels out the vertical translation applied by the scrollview and keeps the y position fixed
          scrollableChild.transform = CGAffineTransformMakeTranslation(0, yOffset);
        }

        if (![scrollableChild shouldScrollHorizontally]) {
          CGFloat scrollLeft = scrollView.contentOffset.x + self.contentInset.left;

          // adjust the x offset based on the current zoom scale
          // if we're zoomed in the offset required will be less, if we're zoomed out it will be more
          CGFloat xOffset = scrollLeft / scrollView.zoomScale;

          // translate the vertically scrolling subview by the calculated x offset
          // this cancels out the horizontal translation applied by the scrollview and keeps the x position fixed
          scrollableChild.transform = CGAffineTransformMakeTranslation(xOffset, 0);
        }
    }
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    [super scrollViewWillBeginDragging:scrollView];
    if ([self.delegate respondsToSelector:@selector(scrollViewWillBeginDragging)]) {
        [self.delegate scrollViewWillBeginDragging];
    }
}

-(void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    [super scrollViewDidEndDragging:scrollView willDecelerate:decelerate];
    if ([self.delegate respondsToSelector:@selector(scrollViewDidEndDragging)]) {
        [self.delegate scrollViewDidEndDragging];
    }
}

@end

@implementation DirectedScrollViewManager

RCT_EXPORT_MODULE()

@synthesize bridge = _bridge;

- (UIView *)view
{
    DirectedScrollView *directedScrollView = [[DirectedScrollView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];

    directedScrollView.delegate = self;

    return directedScrollView;
}

// RCTDirectedScrollViewDelegate methods

-(void)scrollViewWillBeginDragging {
    [self.bridge.eventDispatcher sendDeviceEventWithName:@"scrollViewWillBeginDragging" body:nil];
}

-(void)scrollViewDidEndDragging {
    [self.bridge.eventDispatcher sendDeviceEventWithName:@"scrollViewDidEndDragging" body:nil];
}


// RCTScrollView properties

RCT_EXPORT_VIEW_PROPERTY(bounces, BOOL)
RCT_EXPORT_VIEW_PROPERTY(alwaysBounceHorizontal, BOOL)
RCT_EXPORT_VIEW_PROPERTY(alwaysBounceVertical, BOOL)
RCT_EXPORT_VIEW_PROPERTY(bouncesZoom, BOOL)
RCT_EXPORT_VIEW_PROPERTY(maximumZoomScale, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(minimumZoomScale, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(showsHorizontalScrollIndicator, BOOL)
RCT_EXPORT_VIEW_PROPERTY(showsVerticalScrollIndicator, BOOL)
RCT_EXPORT_VIEW_PROPERTY(canCancelContentTouches, BOOL)
RCT_EXPORT_VIEW_PROPERTY(centerContent, BOOL)
RCT_EXPORT_VIEW_PROPERTY(automaticallyAdjustContentInsets, BOOL)
RCT_EXPORT_VIEW_PROPERTY(decelerationRate, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(directionalLockEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(scrollEnabled, BOOL)
RCT_REMAP_VIEW_PROPERTY(pinchGestureEnabled, scrollView.pinchGestureEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(contentInset, UIEdgeInsets)
RCT_EXPORT_VIEW_PROPERTY(scrollIndicatorInsets, UIEdgeInsets)
RCT_EXPORT_VIEW_PROPERTY(snapToInterval, int)
RCT_EXPORT_VIEW_PROPERTY(scrollEventThrottle, NSTimeInterval)
RCT_EXPORT_VIEW_PROPERTY(snapToAlignment, NSString)
RCT_EXPORT_VIEW_PROPERTY(onScrollBeginDrag, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onScroll, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onScrollEndDrag, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMomentumScrollBegin, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onMomentumScrollEnd, RCTDirectEventBlock)

// RCTScrollView methods

RCT_EXPORT_METHOD(scrollTo:(nonnull NSNumber *)reactTag
                  offsetX:(CGFloat)x
                  offsetY:(CGFloat)y
                  animated:(BOOL)animated)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry){
         UIView *view = viewRegistry[reactTag];
         if ([view conformsToProtocol:@protocol(RCTScrollableProtocol)]) {
             [(id<RCTScrollableProtocol>)view scrollToOffset:(CGPoint){x, y} animated:animated];
         } else {
             RCTLogError(@"tried to scrollTo: on non-RCTScrollableProtocol view %@ with tag #%@", view, reactTag);
         }
     }];
}

RCT_EXPORT_METHOD(zoomToStart:(nonnull NSNumber *)reactTag
                  animated:(BOOL)animated)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry){
         UIView *view = viewRegistry[reactTag];
         if ([view conformsToProtocol:@protocol(RCTScrollableProtocol)]) {
             [(id<RCTScrollableProtocol>)view zoomToRect:CGRectMake(0, 0, 0, 0) animated:animated];
             [((RCTScrollView*)view).scrollView setZoomScale:1.0 animated:animated];
         } else {
             RCTLogError(@"tried to zoomToRect: on non-RCTScrollableProtocol view %@ with tag #%@", view, reactTag);
         }
     }];
}

@end
