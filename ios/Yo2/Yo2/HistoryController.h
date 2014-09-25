//
//  HistoryController.h
//  Yo2
//
//  Created by student on 12/09/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HistoryController : UITableViewController
@property (strong, nonatomic) IBOutlet UINavigationBar* barOutlet;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *buttonOutlet;
@property (nonatomic, strong) NSMutableArray* history;

- (void)setHistory:(NSMutableArray *)newHist;

@end
