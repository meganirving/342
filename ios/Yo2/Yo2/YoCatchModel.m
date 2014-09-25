//
//  YoCatchModel.m
//  Yo2
//
//  Created by student on 8/09/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import "YoCatchModel.h"

@implementation YoCatchModel

+ (NSString*) defaultYoMessage
{
    return @"Yo";
};

-(void) Init:(NSString*)Name and:(NSString*)Message
{
    self.name = Name;
    self.message = Message;
}

-(void) encodeWithCoder:(NSCoder *)coder
{  
    [ coder encodeObject:self.name forKey:@"YoModelName" ];
    [ coder encodeObject:self.message forKey:@"YoModelMsg" ];
}

-(id)initWithCoder:(NSCoder*)coder
{
    self = [super init];
    if (self)
    {
        _name = [[coder decodeObjectForKey:@"YoModelName"] copy];
        _message = [[coder decodeObjectForKey:@"YoModelMsg" ] copy];
    }
    
    return self;
}

@end
