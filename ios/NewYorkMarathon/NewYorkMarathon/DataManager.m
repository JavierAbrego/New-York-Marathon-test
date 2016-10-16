//
//  DataManager.m
//  NewYorkMarathon
//
//  Created by Javier Abrego on 14/10/16.
//  Copyright © 2016 Javier Abrego. All rights reserved.
//

#import "DataManager.h"
#import "AFNetworking.h"

#define kWebserviceBase @"http://192.168.1.107:8080/adidas_dev_test"
#define kWebserviceUserConfig @"%@/user/fields"
#define kWebserviceUserSave @"%@/user/save?%@"

@interface DataManager ()


@end

@implementation DataManager

NSDictionary* fields;

+(DataManager *)sharedInstance{
    static DataManager *sharedInstance = nil;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        sharedInstance = [[DataManager alloc] init];
    });
    
   
    return sharedInstance;
}

-(instancetype)init{
    self = [super init];
    
    if (self){
        //Do init stuff
    }
    
    return self;
}


- (void) fetchFieldsWithDelegate:(id<DataManagerDelegate>)delegate{
    NSString *urlString = [NSString stringWithFormat:kWebserviceUserConfig, kWebserviceBase];
    
    // 1
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:urlString]];
    
    // 2
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    operation.responseSerializer = [AFJSONResponseSerializer serializer];
    
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        // 3
        NSDictionary * response = (NSDictionary *)responseObject;
        NSDictionary *data = response;
        if (data){
            fields=data;
            if ([delegate respondsToSelector:@selector(fetchFieldsDidFinish:)]){
                [delegate fetchFieldsDidFinish:data];
            }
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        
        // 4
        NSLog(@"Error: %@", error);
        if ([delegate respondsToSelector:@selector(fetchFieldsDidFail)]){
            [delegate fetchFieldsDidFail];
        }
    }];
    
    // 5
    [operation start];
}

- (void) saveUserDetailsWithDelegate:(NSString*)params :(id<DataManagerDelegate>)delegate {
    NSString *urlString = [NSString stringWithFormat:kWebserviceUserSave, kWebserviceBase, params];
    
    // 1
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:urlString]];
    
    // 2
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:request];
    operation.responseSerializer = [AFJSONResponseSerializer serializer];
    
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        // 3
        if ([delegate respondsToSelector:@selector(saveUserDetailsDidFinish)]){
            [delegate saveUserDetailsDidFinish];
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        // 4
        NSLog(@"Error: %@", error);
        if ([delegate respondsToSelector:@selector(saveUserDetailsDidFail)]){
            [delegate saveUserDetailsDidFail];
        }
    }];
    
    // 5
    [operation start];
}




@end
