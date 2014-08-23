//
//  ViewController.m
//  Yo2
//
//  Created by student on 14/08/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import "ViewController.h"


// customising uicolor
@interface UIColor (randColor)
+ (UIColor *)random;
@end

@implementation UIColor (randColor)

+ (UIColor *) random
{
    // get 3 random numbers
    double r = drand48();
    double b = drand48();
    double g = drand48();
    
    // make sure it won't be too dark
    float min = 0.2f;
    if (r < min)
        r = min;
    if (g < min)
        g = min;
    if (b < min)
        b = min;

    UIColor* col = [ UIColor colorWithRed:(r) green:(g) blue:b alpha:(1.0) ];
    
    // return colour
    return col;
}

@end

// viewcontroller
@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    self.NameOutlet.delegate = self;
    self.YoOutlet.delegate = self;
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// get button press
- (IBAction)showMessageButtonPressed:(id) sender
{
    [ self showMessage:sender ];
}
// get return key
- (IBAction)showMessageReturnPressed:(UITextField*) sender
{
    [ self showMessage:sender ];
}

// hide keyboard when textfield is done
-(IBAction)hideKeyboard:(id) sender
{
    [ sender resignFirstResponder ];
}

// update message
- (void)showMessage:(id) sender
{
    // start animation
    [UIView beginAnimations:@"fade" context:nil];
    [UIView setAnimationDuration:1];
    // change background to random colour
    [[self view] setBackgroundColor: [UIColor random]];
    [UIView commitAnimations];
    
    // capitalise strings
    NSString *first = [self.YoOutlet.text uppercaseString];
    NSString *second = [self.NameOutlet.text uppercaseString];
    
    
    self.LabelOutlet.text = [first stringByAppendingFormat:@"\n %@", second];
}

@end
