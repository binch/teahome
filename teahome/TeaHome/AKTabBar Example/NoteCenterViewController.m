//
//  NoteCenterViewController.m
//  TeaHome
//
//  Created by andylee on 14-6-28.
//  Copyright (c) 2014年 Ali Karagoz. All rights reserved.
//

#import "NoteCenterViewController.h"
#import "ThreadReplysViewController.h"
#import "AnswersViewController.h"

#define mark_read_atmessage_cmd @"mark_read_atmessage"

@interface NoteCenterViewController ()

@end

@implementation NoteCenterViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];

    self.view.backgroundColor = [UIColor whiteColor];
    
    self.seg = [[UISegmentedControl alloc] initWithItems:[NSArray arrayWithObjects:@"未读",@"已读", nil]];
    self.seg.frame = CGRectMake(0, 0, 160, 30);
    [self.seg addTarget:self action:@selector(handleSegmentControlEvent:) forControlEvents:UIControlEventValueChanged];
    [self.seg setSelectedSegmentIndex:0];
//    [self handleSegmentControlEvent:nil];
    self.navigationItem.titleView = self.seg;
   
    self.tableView = [[UITableView alloc] initWithFrame:self.view.bounds];
    self.tableView.backgroundColor = [UIColor clearColor];
    self.tableView.autoresizingMask = UIViewAutoresizingFlexibleHeight;
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    [self.view addSubview:self.tableView];
    
    [self.tableView addHeaderWithTarget:self action:@selector(headerRereshing)];
    //[self.tableView headerBeginRefreshing];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleSegmentControlEvent:) name:kRefreshNoteCenterViewNotification object:nil];
}

-(void)handleSegmentControlEvent:(id)sender
{
    [self.tableView headerBeginRefreshing];
}

-(void)headerRereshing
{
//    TeaHomeAppDelegate.unreadMessages = [NSMutableArray array];
//    TeaHomeAppDelegate.readMessages = [NSMutableArray array];
    [self.tableView reloadData];
    [self.tableView headerEndRefreshing];
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        [TeaHomeAppDelegate.timer fire];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.tableView reloadData];
        });
        
    });
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidAppear:(BOOL)animated
{
    [self.tableView headerBeginRefreshing];
}

#pragma mark -- table view delegate methods
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *identifier = @"My-cell";
    UITableViewCell *cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    NSDictionary *message = nil;
    if (self.seg.selectedSegmentIndex == 0) {
        message = TeaHomeAppDelegate.unreadMessages[indexPath.row];
    }else{
        message = TeaHomeAppDelegate.readMessages[indexPath.row];
        
    }
    
    NSString *text = [message objectForKey:@"text"];
    cell.textLabel.numberOfLines = 0;
    cell.textLabel.font = [UIFont systemFontOfSize:13];
    cell.textLabel.text = text;
    
    return cell;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSDictionary *message = nil;
    if (self.seg.selectedSegmentIndex == 0) {
        message = TeaHomeAppDelegate.unreadMessages[indexPath.row];
    }else{
        message = TeaHomeAppDelegate.readMessages[indexPath.row];
    }
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:[TeaHomeAppDelegate.unreadMessages count]];
    NSString *type = [message objectForKey:@"type"];
    int messageId = [[message objectForKey:@"id"] intValue];
    int from_id = [[message objectForKey:@"from_id"] intValue];
    
    NSString *url = nil;
    if ([type isEqualToString:@"forum"]) {
        //帖子
        url = [NSString stringWithFormat:@"%@%@&thread=%d",CMD_URL,update_comment_cmd,from_id];
        id json = [Utils getJsonDataFromWeb:url];
        if (json != nil) {
            NSDictionary *thread = (NSDictionary *)json;
            ThreadReplysViewController *tcvc = [[ThreadReplysViewController alloc] init];
            tcvc.thread = thread;
            [self.navigationController pushViewController:tcvc animated:YES];
        }else{
            [Utils showAlertViewWithMessage:@"网络连接出错,请稍后再试."];
        }
    }else if([type isEqualToString:@"qa"]){
        url = [NSString stringWithFormat:@"%@%@&question=%d",CMD_URL,update_answer_cmd,from_id];
        id json = [Utils getJsonDataFromWeb:url];
        if (json != nil) {
            NSDictionary *question = (NSDictionary *)json;
            AnswersViewController *avc = [[AnswersViewController alloc] init];
            avc.question = question;
            [self.navigationController pushViewController:avc animated:YES];
        }else{
            [Utils showAlertViewWithMessage:@"网络连接出错,请稍后再试."];
        }
    }
    
    
    if (self.seg.selectedSegmentIndex == 0) {
        NSString *url = [NSString stringWithFormat:@"%@%@&atmessage=%d&username=%@",CMD_URL,mark_read_atmessage_cmd,messageId,TeaHomeAppDelegate.username];
        id json = [Utils getJsonDataFromWeb:url];
        if (json != nil) {
            if ([[json objectForKey:@"ret"] isEqualToString:@"ok"]) {
                [[NSNotificationCenter defaultCenter] postNotificationName:kReadAtmessageNotification object:message];
            }
        }else{
            [Utils showAlertViewWithMessage:@"网络连接出错,请稍后再试."];
        }
    }
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 44;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    if (self.seg.selectedSegmentIndex == 0) {
        return [TeaHomeAppDelegate.unreadMessages count];
    }
    return [TeaHomeAppDelegate.readMessages count];
}
@end
