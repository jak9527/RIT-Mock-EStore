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

  constructor(
    private auctionService: AuctionService,
    private currentUserService: CurrentUserService
    ) { }

  ngOnInit(): void {
    this.getAuction();
    var theUser: User = null as unknown as User;
        this.currentUserService.getCurrentUser().subscribe(curUser =>{
            theUser = curUser;
            if (theUser.id == 0){
                this.isAdmin = true;
            } else {
                this.isAdmin = false;
            }
        });
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.auctionService.addAuction(1, "admin", 3.0, "2023-04-05-06:25:00", { name } as Product)
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

  save(): void {
    if (this.auction) {
        console.log(this.auction.maxBid);
      this.auctionService.addAuction(1, this.auction.maxBid.user, this.auction.maxBid.bid, "2023-04-05-06:25:00", this.auction.product)
        .subscribe();
    }
  }
}