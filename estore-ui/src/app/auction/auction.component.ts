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

  constructor(
    private auctionService: AuctionService,
    private currentUserService: CurrentUserService
    ) { }

  ngOnInit(): void {
    this.getAuction();
    this.getIsRunning();
    console.log(this.isOver);
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

  add(name: string, quantity: number, start: number, endTime: string): void {
    name = name.trim();
    if (!name) { return; }
    this.auctionService.addAuction(1, "no bidders yet!", start, this.parseTime(endTime), { name, quantity } as Product)
      .subscribe(auction => {
        this.auction = auction;
        console.log(auction);
        console.log(this.auction.maxBid);
      });
  }

  getAuction(): void {
    this.auctionService.getAuction()
      .subscribe(auction => this.auction = auction);
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
    var theUser: User = null as unknown as User;
        this.currentUserService.getCurrentUser().subscribe(curUser =>{
            theUser = curUser;
            this.getAuction();
            // console.log(this.auction?.maxBid);
            //Resolve with team. placeBid in controller should return old bid if new bid cannot be placed
            this.auctionService.newBid(theUser.username, amount)
                .subscribe(newBid => {
                     this.auction!.maxBid = newBid});
        }); 
  }

  parseTime(time: string): string {
    time = time + "";
    // let split: string[] = time.split(",", 6);
    var split = time.split(",", 6);
    var result = "";

    result += split[0]; //year
    result += "-";
    if(split[1].length == 1){ //month
        result += "0"+split[1];
    }
    else{
        result += split[1];
    }
    result += "-";
    if(split[2].length == 1){ //day
        result += "0"+split[2];
    }
    else{
        result += split[2];
    }
    result += "-";
    if(split[3].length == 1){ //hour
        result += "0"+split[3];
    }
    else{
        result += split[3];
    }
    result += ":";
    if(split[4].length == 1){ //minute
        result+= "0"+split[4];
    }
    else{
        result += split[4];
    }
    result += ":";
    if(split.length == 6){
        if(split[5].length == 1){
            result += "0" + split[5];
        }
        else{
            result += split[5];
        }
    }
    else{
        result += "00";
    }

    return result;
  }
}