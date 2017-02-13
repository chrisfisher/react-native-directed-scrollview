//
//  DirectedScrollViewChildManager.h
//  DirectedScrollViewChildManager
//

#import <Foundation/Foundation.h>
#import <React/RCTViewManager.h>
#import <React/RCTView.h>

@interface DirectedScrollViewChild : RCTView

@property (nonatomic, strong) NSString *scrollDirection;

- (BOOL)shouldScrollHorizontally;

- (BOOL)shouldScrollVertically;

@end

@interface DirectedScrollViewChildManager : RCTViewManager

@end
