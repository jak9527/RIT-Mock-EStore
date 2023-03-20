import { Component, OnInit } from '@angular/core';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { MessageService } from '../message.service';
import { CartService } from '../cart.service';
import { CurrentUserService } from '../currentUser.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
    products: Product[] = [];
  
    constructor(
      private productService: ProductService,
      private cartService: CartService,
      private currentUserService: CurrentUserService
      ) { }
  
    ngOnInit(): void {
      this.getProducts();
    }
  
    getProducts(): void {
      this.productService.getProducts()
      .subscribe(products => this.products = products);
    }

    addToCart(product: Product): void {
      this.currentUserService.getCurrentUser().subscribe((user) => {
            this.cartService.addProductToCart(user.id,product).subscribe();
            this.ngOnInit();
      });
  }

    add(name: string): void {
        name = name.trim();
        if (!name) { return; }
        this.productService.addProduct({ name } as Product)
          .subscribe(product => {
            this.products.push(product);
          });
      }

    delete(product: Product): void {
        this.products = this.products.filter(h => h !== product);
        this.productService.deleteProduct(product.id).subscribe();
    }
  }