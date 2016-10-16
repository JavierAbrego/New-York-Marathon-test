//
//  ViewController.m
//  NewYorkMarathon
//
//  Created by Javier Abrego on 14/10/16.
//  Copyright © 2016 Javier Abrego. All rights reserved.
//

#import "RegisterController.h"
#import "DataManager.h"

@interface RegisterController ()
@property (weak, nonatomic) IBOutlet UIView *formView;

@end

@implementation RegisterController
float y = 0;
float kyIncrement = 25;
UIActivityIndicatorView *spinner;
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
}

-(void)viewWillAppear:(BOOL)animated{
    [self initUI];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (void)initUI {
    [self appendSpinner];
    [spinner startAnimating];
    [[DataManager sharedInstance] fetchFieldsWithDelegate:self];
}

-(void)fetchFieldsDidFinish:(NSDictionary*) data{
    for(NSDictionary *field in data){
        [self appendInputField:field[@"fieldName"] : field[@"text"] : field[@"color"]];
    }
    [self appendButton:@"Register"];
    [spinner stopAnimating];
    
}

-(void)fetchFieldsDidFail{
    [self showAlert:@"No network connection" :@"You must be connected to the internet to use this app."];
    [spinner stopAnimating];
}



-(void)appendSpinner{
    spinner = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
    spinner.center = CGPointMake(160, 240);
    spinner.tag = 12;
    [_formView addSubview:spinner];
}

NSMutableDictionary *fieldDictionary;

-(void)appendInputField: (NSString*)fieldName: (NSString*)fieldNameText : (NSString*)color{
    int width = _formView.bounds.size.width;
    int height = 20;
    UILabel *labelName = [UILabel new];
    labelName.textColor= [UIColor blackColor];
    labelName.text = fieldNameText;
    labelName.frame=CGRectMake(0, y+=kyIncrement, width, height);
    [_formView addSubview:labelName];
    
    UITextField *field = [UITextField new];
    field.placeholder = [NSString stringWithFormat:@"Write your %@ here",fieldNameText];
    field.frame=CGRectMake(0, y+=kyIncrement, width, height);
    [_formView addSubview:field];
    if(!fieldDictionary){
        fieldDictionary = [NSMutableDictionary new];
    }
    [fieldDictionary setValue:field forKey:fieldName];
}

-(void)appendButton:(NSString*)buttonText{
    UIButton *button = [UIButton new];
    [button setTitle:buttonText forState:UIControlStateNormal];
    [button setFrame:CGRectMake(0, y+=kyIncrement+10, 200, 25)];
    button.backgroundColor = [UIColor blueColor];
    [button addTarget:self action:@selector(registerAction:) forControlEvents:UIControlEventTouchUpInside];
    [_formView addSubview:button];
}

-(void)registerAction :(UIButton*)sender
{
    NSString *bName=sender.titleLabel.text;
    NSLog(bName);
    NSMutableString *params = [NSMutableString string];
    for (NSString* key in fieldDictionary) {
        UITextField *field = fieldDictionary[key];
        NSString *content = [NSString stringWithFormat:@"%@=%@&", key, field.text];
        [params appendString:content];
    }
     NSLog(params);
    [[DataManager sharedInstance] saveUserDetailsWithDelegate: params: self ];
}

-(void)saveUserDetailsDidFinish{
    [self showAlert:@"Register" : @"You have been registered in the event successfully. "];
}

-(void)saveUserDetailsDidFail{
    [self showAlert:@"Register" : @"There was an error in the internet connection, please try again later. "];
}

-(void) showAlert:(NSString*)title : (NSString*)message{
    UIAlertController * alert = [UIAlertController
                                 alertControllerWithTitle:title
                                 message:message
                                 preferredStyle:UIAlertControllerStyleAlert];
    
    UIAlertAction* okButton = [UIAlertAction
                                actionWithTitle:@"OK"
                                style:UIAlertActionStyleDefault
                                handler:nil];
    
    
    [alert addAction:okButton];
    
    [self presentViewController:alert animated:YES completion:nil];
}



@end
