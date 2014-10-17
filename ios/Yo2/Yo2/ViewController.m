//
//  ViewController.m
//  Yo2
//
//  Created by student on 14/08/2014.
//  Copyright (c) 2014 University of Wollongong. All rights reserved.
//

#import "ViewController.h"
#import "YoCatchModel.h"
#import "HistoryController.h"


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
    
    srandom(time(NULL));
    
    // set localised strings
    self.NameOutlet.placeholder = NSLocalizedString(@"Name", nil);
    self.YoOutlet.placeholder = NSLocalizedString(@"Yo", nil);
    self.LabelOutlet.text = NSLocalizedString(@"Msg", nil);
    [self.ButtonOutlet setTitle:NSLocalizedString(@"History", nil) forState:UIControlStateNormal];
     
    self.NameOutlet.delegate = self;
    self.YoOutlet.delegate = self;
    
    // set up audio player
    NSArray *soundList = [[NSArray alloc] initWithObjects:@"%@/yo.mp3", @"%@/SC_Yo1.mp3", @"%@/SC_Yo2.mp3", @"%@/SC_Yo3.mp3", @"%@/SC_Yo4.mp3", nil];
    self.urls = [[NSMutableArray alloc] init];
    for (NSString *fileName in soundList)
    {
        NSString* path =[NSString stringWithFormat:fileName, [[NSBundle mainBundle]resourcePath]];
        NSURL* soundURL = [NSURL fileURLWithPath:path];
        NSLog(@"%@", soundURL);
        [self.urls addObject:soundURL];
    }
    
    // load/create history array
    self.filePath = @"save.bin";
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    NSString* docDirect = [paths lastObject];
    NSString* path = [docDirect stringByAppendingPathComponent:self.filePath];
    self.history = [NSKeyedUnarchiver unarchiveObjectWithFile:path];
    if (self.history == NULL)
    {
        self.history = [[NSMutableArray alloc] init];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

// get button press
- (IBAction)showMessageButtonPressed:(id) sender
{
   
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
    
    // update label
    self.LabelOutlet.text = [first stringByAppendingFormat:@"\n %@", second];
    
    // play sound
    int rand = random() % 4;
    NSURL* url = [self.urls objectAtIndex:rand];
    NSError *error = nil;
    NSLog(@"%@", url);
    self.audioPlayer = [[AVAudioPlayer alloc] initWithContentsOfURL:url error:&error];
    [self.audioPlayer play];
    
    // update list
    YoCatchModel *newModel = [[YoCatchModel alloc] init];
    [ newModel Init:second and:first ];
    [ self.history addObject:newModel ];
    
    // serialise array
    NSArray* paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask, YES);
    NSString* docDirect = [paths lastObject];
    NSString* path = [docDirect stringByAppendingPathComponent:self.filePath];
    BOOL success = [NSKeyedArchiver archiveRootObject:self.history toFile:path];
}

-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"MySegue"])
    {
        [segue.destinationViewController setHistory:self.history];
    }
    return;
}

@end
