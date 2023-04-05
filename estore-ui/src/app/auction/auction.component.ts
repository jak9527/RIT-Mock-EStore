import { Component, OnInit } from '@angular/core';
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
  auction: Auction = null as unknown as Auction;
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

  getAuction(): void {
    this.auctionService.getAuction()
      .subscribe(auction => this.auction = auction);
  }
}