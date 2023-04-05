import { Component, OnInit, Input } from '@angular/core';
import { Product } from '../product';
import { Auction } from '../auction';
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
    this.auctionService.addAuction(1, "", 0, "none", { name } as Product)
      .subscribe(auction => {
        this.auction = auction;
      });
  }

  getAuction(): void {
    this.auctionService.getAuction()
      .subscribe(auction => this.auction = auction);
  }

  save(): void {
    if (this.auction) {
      this.auctionService.addAuction(1, "", this.auction.bid, this.auction.endTime, this.auction.product)
        .subscribe();
    }
  }
}