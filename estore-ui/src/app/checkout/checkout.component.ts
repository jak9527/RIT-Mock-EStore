import { Component, Input, OnInit } from '@angular/core';
import { Cart } from '../cart';
import { CartService } from '../cart.service';
import { CurrentUserService } from '../currentUser.service';
import { Product } from '../product';
import { Location } from '@angular/common';
import { ProductService } from '../product.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
    @Input()
  cart!: Cart;
    @Input()
  total!: number;
    @Input()
  products!: Map<number, Product>;

    constructor(
        private cartService: CartService,
        private location: Location,
        private productService: ProductService,
        private currentUserService: CurrentUserService
      ) {}

    ngOnInit(): void {
        this.getCart();
    }
      
    getCart(): void {
      this.currentUserService.getCurrentUser().subscribe((user) => {
          this.cartService.getCart(user.id).subscribe((cart => {
            this.cart = cart;
            this.products = this.cart.products;
            this.total = this.cart.total;
          }))
      });
    }

    goBack(): void {
        this.location.back();
    }

    checkout(): void {
        this.cartService.checkoutProduct(this.cart.id).subscribe();
        this.ngOnInit();
    }

}

