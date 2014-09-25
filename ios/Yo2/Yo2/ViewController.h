//
//  ViewController.h
//  Yo2
//
//  Created by student on 14/08/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>

@interface ViewController : UIViewController

// regular data
@property (nonatomic, strong) NSMutableArray* history;
@property (nonatomic, strong) NSString* filePath;
@property (nonatomic, strong) NSMutableArray* urls;

// outlets
@property (strong, nonatomic) IBOutlet UIButton *ButtonOutlet;
@property (strong, nonatomic) IBOutlet UILabel *LabelOutlet;
@property (strong, nonatomic) IBOutlet UITextField *YoOutlet;
@property (strong, nonatomic) IBOutlet UITextField *NameOutlet;
@property (strong, nonatomic) AVAudioPlayer* audioPlayer;

// functions
-(IBAction)hideKeyboard:(id)sender;
-(IBAction)showMessageButtonPressed:(id)sender;
-(IBAction)showMessageReturnPressed:(UITextField*)sender;
-(void)showMessage:(id)sender;

@end
