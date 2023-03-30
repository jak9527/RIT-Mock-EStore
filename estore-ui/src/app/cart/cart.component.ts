import { ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { Cart } from '../cart';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { CartService } from '../cart.service';
import { CurrentUserService } from '../currentUser.service';
import { switchMap, map } from 'rxjs/operators';
import { Product } from '../product';
import { tick } from '@angular/core/testing';
import { ProductService } from '../product.service';
import { __values } from 'tslib';
import { forkJoin } from 'rxjs';

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

  disabledButtons: { [key: string]: boolean } = {};

    constructor(
        private cartService: CartService,
        private location: Location,
        private currentUserService: CurrentUserService,
        private productService: ProductService
      ) {}

    ngOnInit(): void {
        this.getCart();
    }
      
    getCart(): void {
      this.currentUserService.getCurrentUser().pipe(
        switchMap((user) => this.cartService.getCart(user.id)),
        switchMap((cart) => {
          this.cart = cart;
          this.products = this.cart.products;
          const productObservables = Object.values(this.products).map((value) => 
            this.productService.getProduct(value.id).pipe(
              map((product2) => {
                return {
                  ...value,
                  disabled: product2.quantity - value.quantity === 0
                };
              })
            )
          );
          return forkJoin(productObservables);
        })
      ).subscribe((updatedProducts) => {
        for (const product of updatedProducts) {
          this.disabledButtons[product.id] = product.disabled;
        }
      });
    }

    goBack(): void {
        this.location.back();
    }

    decrease(product: Product): void {
      this.cartService.updateProductCount(this.cart.id, product.id, -1).subscribe();
      this.ngOnInit();
  }


    add(product: Product): void {
      this.cartService.updateProductCount(this.cart.id, product.id, 1).subscribe();
      this.ngOnInit();
  }

    remove(product: Product): void {
      this.cartService.updateProductCount(this.cart.id, product.id, -product.quantity).subscribe();
      this.ngOnInit();
  }

  


}
