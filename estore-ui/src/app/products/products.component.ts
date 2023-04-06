import { Component, OnInit } from '@angular/core';

import { Product } from '../product';
import { ProductService } from '../product.service';
import { MessageService } from '../message.service';
import { CartService } from '../cart.service';
import { CurrentUserService } from '../currentUser.service';
import { User } from '../user';
import { MapType } from '@angular/compiler';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {
    products: Product[] = [];
    isAdmin: boolean = false;
    isEmpty: boolean = false;
  
    constructor(
      private productService: ProductService,
      private cartService: CartService,
      private currentUserService: CurrentUserService
      ) { }
  
    ngOnInit(): void {
      this.getProducts();
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
  
    getProducts(): void {
      this.productService.getProducts()
      .subscribe(products => {
        this.products = products;
        if(this.products.length == 0) {
          this.isEmpty = true;
        }
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

  }