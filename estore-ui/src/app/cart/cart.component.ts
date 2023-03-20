import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { Cart } from '../cart';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { CartService } from '../cart.service';
import { CurrentUserService } from '../currentUser.service';
import { switchMap } from 'rxjs';
import { Product } from '../product';
import { tick } from '@angular/core/testing';

@Component({
  selector: 'app-product-detail',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
    @Input()
  cart!: Cart;
    @Input()
  products!: Map<number, Product>;

    constructor(
        private cartService: CartService,
        private location: Location,
        private currentUserService: CurrentUserService,
      ) {}

    ngOnInit(): void {
        this.getCart();
    }
      
    getCart(): void {
      this.currentUserService.getCurrentUser().subscribe((user) => {
        //if( user.id != 0 ){
          this.cartService.getCart(user.id).subscribe((cart => {
            this.cart = cart;
            this.products = this.cart.products;
          }))
        //}
      });
    }

    goBack(): void {
        this.location.back();
    }

    remove(product: Product): void {
      this.cartService.updateProductCount(this.cart.id, product.id, -1).subscribe();
      this.ngOnInit();
  }

    add(product: Product): void {
      this.cartService.updateProductCount(this.cart.id, product.id, 1).subscribe();
      this.ngOnInit();
  }


}
