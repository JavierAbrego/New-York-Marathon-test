//
//  DataManager.h
//  NewYorkMarathon
//
//  Created by Javier Abrego on 14/10/16.
//  Copyright © 2016 Javier Abrego. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol DataManagerDelegate <NSObject>

    @optional
    -(void)fetchFieldsDidFinish: (NSDictionary*)data;
    @optional
    -(void)fetchFieldsDidFail;
    @optional
    -(void)saveUserDetailsDidFinish;
    @optional
    -(void)saveUserDetailsDidFail;
@end

@interface DataManager : NSObject

+ (DataManager*) sharedInstance;

-(void)fetchFieldsWithDelegate:(id<DataManagerDelegate>)delegate;
-(void)saveUserDetailsWithDelegate:(id<DataManagerDelegate>)delegate : (NSString*)params;

@end
