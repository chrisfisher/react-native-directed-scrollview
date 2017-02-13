//
//  DirectedScrollViewManager.h
//  DirectedScrollViewManager
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>

@protocol DirectedScrollViewDelegate <NSObject>

-(void)scrollViewWillBeginDragging;
-(void)scrollViewDidEndDragging;

@end

@interface DirectedScrollViewManager : RCTViewManager <DirectedScrollViewDelegate>

@end
