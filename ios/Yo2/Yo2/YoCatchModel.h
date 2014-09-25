//
//  YoCatchModel.h
//  Yo2
//
//  Created by student on 8/09/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YoCatchModel : NSObject <NSCoding>

@property (nonatomic, strong) NSString* name;
@property (nonatomic, strong) NSString* message;

+ (NSString*) defaultYoMessage;
- (void)Init:(NSString*)Name and:(NSString*)Message;

- (void)encodeWithCoder:(NSCoder*) coder;

@end
