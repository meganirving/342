//
//  ViewController.h
//  Yo2
//
//  Created by student on 14/08/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ViewController : UIViewController

// outlets
@property (strong, nonatomic) IBOutlet UILabel *LabelOutlet;
@property (strong, nonatomic) IBOutlet UITextField *YoOutlet;
@property (strong, nonatomic) IBOutlet UITextField *NameOutlet;

// functions
-(IBAction)hideKeyboard:(id)sender;
-(IBAction)showMessageButtonPressed:(id)sender;
-(IBAction)showMessageReturnPressed:(UITextField*)sender;
-(void)showMessage:(id)sender;

@end
