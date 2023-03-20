import { Component, Input } from '@angular/core';
import { Cart } from '../cart';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { CartService } from '../cart.service';
import { CurrentUserService } from '../currentUser.service';
import { switchMap } from 'rxjs';
import { Product } from '../product';

@Component({
  selector: 'app-product-detail',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent {
    @Input() cart?: Cart;
    @Input() products?: Map<number, Product>;

    constructor(
        private route: ActivatedRoute,
        private cartService: CartService,
        private location: Location,
        private currentUserService: CurrentUserService
      ) {}

    ngOnInit(): void {
        this.getCart();
    }
      
    getCart(): void {
      this.currentUserService.getCurrentUser().subscribe((user) => {
        if( user.id != 0 ){
          this.cartService.getCart(user.id).subscribe((cart => {
            this.cart = cart;
            this.products = this.cart.products
          }))
        }
      });
    }

    goBack(): void {
        this.location.back();
    }

}
