//
//  DirectedScrollViewManager.m
//  DirectedScrollViewManager
//
//  Created by Chris Fisher on 23/11/16.
//

#import "DirectedScrollViewManager.h"
#import "RCTScrollView.h"

@interface DirectedScrollView : RCTScrollView

@property (nonatomic) int horizontallyScrollingSubviewIndex;
@property (nonatomic) int verticallyScrollingSubviewIndex;

@end

@implementation DirectedScrollView

#pragma mark - ScrollView delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (_horizontallyScrollingSubviewIndex <= 0 || _verticallyScrollingSubviewIndex <= 0) return;
    
    UIView *contentView = [self contentView];
    
    NSUInteger subviewCount = contentView.reactSubviews.count;
    
    if (_horizontallyScrollingSubviewIndex >= subviewCount || _verticallyScrollingSubviewIndex >= subviewCount) return;
    
    UIView *horizontallyScrollingSubview = contentView.reactSubviews[_horizontallyScrollingSubviewIndex];
    UIView *verticallyScrollingSubview = contentView.reactSubviews[_verticallyScrollingSubviewIndex];
    
    CGFloat scrollLeft = scrollView.contentOffset.x + self.contentInset.left;
    CGFloat scrollTop = scrollView.contentOffset.y + self.contentInset.top;
    
    // adjust the x and y offsets based on the current zoom scale
    // if we're zoomed in the offset required will be less, if we're zoomed out it will be more
    CGFloat xOffset = scrollLeft / scrollView.zoomScale;
    CGFloat yOffset = scrollTop / scrollView.zoomScale;
    
    // translate the horizontally scrolling subview by the calculated y offset
    // this cancels out the vertical translation applied by the scrollview and keeps the y position fixed
    horizontallyScrollingSubview.transform = CGAffineTransformMakeTranslation(0, yOffset);
    
    // translate the vertically scrolling subview by the calculated x offset
    // this cancels out the horizontal translation applied by the scrollview and keeps the x position fixed
    verticallyScrollingSubview.transform = CGAffineTransformMakeTranslation(xOffset, 0);
}

@end

@implementation DirectedScrollViewManager

RCT_EXPORT_MODULE()

- (UIView *)view
{
    return [[DirectedScrollView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

#pragma mark - export custom properties

RCT_EXPORT_VIEW_PROPERTY(horizontallyScrollingSubviewIndex, int)
RCT_EXPORT_VIEW_PROPERTY(verticallyScrollingSubviewIndex, int)
RCT_EXPORT_VIEW_PROPERTY(alwaysBounceHorizontal, BOOL)
RCT_EXPORT_VIEW_PROPERTY(alwaysBounceVertical, BOOL)
RCT_EXPORT_VIEW_PROPERTY(bounces, BOOL)
RCT_EXPORT_VIEW_PROPERTY(bouncesZoom, BOOL)
RCT_EXPORT_VIEW_PROPERTY(canCancelContentTouches, BOOL)
RCT_EXPORT_VIEW_PROPERTY(centerContent, BOOL)
RCT_EXPORT_VIEW_PROPERTY(automaticallyAdjustContentInsets, BOOL)
RCT_EXPORT_VIEW_PROPERTY(decelerationRate, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(directionalLockEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(maximumZoomScale, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(minimumZoomScale, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(scrollEnabled, BOOL)
RCT_EXPORT_VIEW_PROPERTY(showsHorizontalScrollIndicator, BOOL)
RCT_EXPORT_VIEW_PROPERTY(showsVerticalScrollIndicator, BOOL)
RCT_EXPORT_VIEW_PROPERTY(zoomScale, CGFloat)
RCT_EXPORT_VIEW_PROPERTY(contentInset, UIEdgeInsets)
RCT_EXPORT_VIEW_PROPERTY(scrollIndicatorInsets, UIEdgeInsets)
RCT_EXPORT_VIEW_PROPERTY(snapToInterval, int)
RCT_EXPORT_VIEW_PROPERTY(snapToAlignment, NSString)

@end

