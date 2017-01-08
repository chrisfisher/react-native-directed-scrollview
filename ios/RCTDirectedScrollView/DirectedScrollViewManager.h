//
//  DirectedScrollViewManager.h
//  DirectedScrollViewManager
//
//  Created by Chris Fisher on 23/11/16.
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>

@protocol DirectedScrollViewDelegate <NSObject>

-(void)scrollViewWillBeginDragging;
-(void)scrollViewDidEndDragging;

@end

@interface DirectedScrollViewManager : RCTViewManager <DirectedScrollViewDelegate>

@end
