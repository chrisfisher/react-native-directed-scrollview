//
//  DirectedScrollViewManager.m
//  DirectedScrollViewManager
//
//  Created by Chris Fisher on 23/11/16.
//

#import "DirectedScrollViewManager.h"
#import "RCTScrollView.h"
#import "RCTUIManager.h"
#import "RCTEventDispatcher.h"

@interface DirectedScrollView : RCTScrollView

@property (nonatomic) int horizontallyScrollingSubviewIndex;
@property (nonatomic) int verticallyScrollingSubviewIndex;

@property (nonatomic, weak) id <DirectedScrollViewDelegate> delegate;

@end

@implementation DirectedScrollView

#pragma mark - ScrollView delegate

- (void)scrollViewDidScroll:(UIScrollView *)scrollView
{
    if (_horizontallyScrollingSubviewIndex < 0 || _verticallyScrollingSubviewIndex < 0) return;
    
    UIView *contentView = [self contentView];
    
    NSUInteger subviewCount = contentView.reactSubviews.count;
    
    if (_horizontallyScrollingSubviewIndex > 0 && _horizontallyScrollingSubviewIndex < subviewCount)
    {
        UIView *horizontallyScrollingSubview = contentView.reactSubviews[_horizontallyScrollingSubviewIndex];
        
        CGFloat scrollTop = scrollView.contentOffset.y + self.contentInset.top;
        
        // adjust the y offset based on the current zoom scale
        // if we're zoomed in the offset required will be less, if we're zoomed out it will be more
        CGFloat yOffset = scrollTop / scrollView.zoomScale;
        
        // translate the horizontally scrolling subview by the calculated y offset
        // this cancels out the vertical translation applied by the scrollview and keeps the y position fixed
        horizontallyScrollingSubview.transform = CGAffineTransformMakeTranslation(0, yOffset);
    }
    
    if (_verticallyScrollingSubviewIndex > 0 && _verticallyScrollingSubviewIndex < subviewCount)
    {
        UIView *verticallyScrollingSubview = contentView.reactSubviews[_verticallyScrollingSubviewIndex];
        
        CGFloat scrollLeft = scrollView.contentOffset.x + self.contentInset.left;
        
        // adjust the x offset based on the current zoom scale
        // if we're zoomed in the offset required will be less, if we're zoomed out it will be more
        CGFloat xOffset = scrollLeft / scrollView.zoomScale;
        
        // translate the vertically scrolling subview by the calculated x offset
        // this cancels out the horizontal translation applied by the scrollview and keeps the x position fixed
        verticallyScrollingSubview.transform = CGAffineTransformMakeTranslation(xOffset, 0);
    }
}

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    if ([self.delegate respondsToSelector:@selector(scrollViewWillBeginDragging)]) {
        [self.delegate scrollViewWillBeginDragging];
    }
}

-(void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
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


// RCTDirectedScrollView properties

RCT_EXPORT_VIEW_PROPERTY(horizontallyScrollingSubviewIndex, int)
RCT_EXPORT_VIEW_PROPERTY(verticallyScrollingSubviewIndex, int)


// RCTDirectedScrollViewDelegate methods

-(void)scrollViewWillBeginDragging {
    [self.bridge.eventDispatcher sendDeviceEventWithName:@"scrollViewWillBeginDragging" body:nil];
}

-(void)scrollViewDidEndDragging {
    [self.bridge.eventDispatcher sendDeviceEventWithName:@"scrollViewDidEndDragging" body:nil];
}


// RCTScrollView properties

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

RCT_EXPORT_METHOD(zoomToFit:(nonnull NSNumber *)reactTag
                  animated:(BOOL)animated)
{
    [self.bridge.uiManager addUIBlock:
     ^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry){
         UIView *view = viewRegistry[reactTag];
         if ([view conformsToProtocol:@protocol(RCTScrollableProtocol)]) {
             CGRect contentViewFrame = [(RCTScrollView*)view contentView].frame;
             if (contentViewFrame.size.width > view.frame.size.width) {
                 [(id<RCTScrollableProtocol>)view zoomToRect:contentViewFrame animated:animated];
             }
         } else {
             RCTLogError(@"tried to zoomToRect: on non-RCTScrollableProtocol view %@ with tag #%@", view, reactTag);
         }
     }];
}

@end