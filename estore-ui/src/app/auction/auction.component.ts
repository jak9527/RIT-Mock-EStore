import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product';
import { Auction } from '../auction';
import { Bid } from '../bid';
import { AuctionService } from '../auction.service';
import { CurrentUserService } from '../currentUser.service';
import { User } from '../user';

@Component({
  selector: 'app-auction',
  templateUrl: './auction.component.html',
  styleUrls: [ './auction.component.css' ]
})
export class AuctionComponent implements OnInit {
  @Input() auction?: Auction;
  isAdmin: boolean = false;
  isOver: Boolean = false;

  targetYear!: number;
  targetMonth!: number;
  targetDay!: number;
  targetHours!: number;
  targetMinutes!: number;
  targetSeconds: number = 0;


  constructor(
    private auctionService: AuctionService,
    private currentUserService: CurrentUserService
    ) { }

  ngOnInit(): void {
    this.getAuction();
    this.getIsRunning();
    var theUser: User = null as unknown as User;
        this.currentUserService.getCurrentUser().subscribe(curUser =>{
            theUser = curUser;
            if (theUser.id == 0){
                this.isAdmin = true;
            } else {
                this.isAdmin = false;
            }
        });
  
    // console.log(this.isRunning);
    // this.auctionService.getAuctionStatus().subscribe(running => this.isRunning = running);
  }

  add(name: string, image: string, quantity: number, start: number, endTime: string): void {
    name = name.trim();
    if (!name) { return; }
    this.auctionService.addAuction(1, "no bidders yet!", start, this.parseTime(endTime), { name, quantity, image } as Product)
      .subscribe(auction => {
        this.auction = auction;
        console.log(auction);
        console.log(this.auction.maxBid);
      });
  }

  getAuction(): void {
    this.auctionService.getAuction()
      .subscribe(auction => {this.auction = auction; this.parseTime(auction.endTime);});
  }

  getIsRunning(): void {
    this.auctionService.getAuctionStatus()
      .subscribe(running => {this.isOver = running;});
  }

  save(): void {
    if (this.auction) {
        console.log(this.auction.maxBid);
        this.auctionService.addAuction(1, this.auction.maxBid.user, this.auction.maxBid.bid, this.parseTime(this.auction.endTime), this.auction.product)
            .subscribe();
    }
  }

  delete(): void {
    if (this.auction) {
      this.auctionService.deleteAuction().subscribe(()=>
        this.getAuction());
    }
    
  }

  placeBid(amount: number): void {
    this.ngOnInit();
    this.auctionService.getAuctionStatus()
      .subscribe(running => {
        if( !running ) {
          var theUser: User = null as unknown as User;
          this.currentUserService.getCurrentUser().subscribe(curUser =>{
            theUser = curUser;
            this.getAuction();
            this.auctionService.newBid(theUser.username, amount)
                .subscribe(newBid => {
                     this.auction!.maxBid = newBid});
        });  }
      });
      this.ngOnInit();
  }

  parseTime(time: string): string {
    time = time + "";
    // let split: string[] = time.split(",", 6);
    var split = time.split(",", 6);
    var result = "";

    result += split[0]; //year
    this.targetYear = split[0] as unknown as number;
    result += "-";
    if(split[1].length == 1){ //month
        result += "0"+split[1];
    }
    else{
        result += split[1];
    }
    this.targetMonth = split[1] as unknown as number;
    result += "-";
    if(split[2].length == 1){ //day
        result += "0"+split[2];
    }
    else{
        result += split[2];
    }
    this.targetDay = split[2] as unknown as number;
    result += "-";
    if(split[3].length == 1){ //hour
        result += "0"+split[3];
    }
    else{
        result += split[3];
    }
    this.targetHours = split[3] as unknown as number;
    result += ":";
    if(split[4].length == 1){ //minute
        result+= "0"+split[4];
    }
    else{
        result += split[4];
    }
    this.targetMinutes = split[4] as unknown as number;
    result += ":";
    if(split.length == 6){
        if(split[5].length == 1){
            result += "0" + split[5];
        }
        else{
            result += split[5];
        }
        this.targetSeconds = split[5] as unknown as number;
    }
    else{
        result += "00";
    }

    return result;
  }
}