import { Component, Input } from '@angular/core';
import { Product } from '../product';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ProductService } from '../product.service';
import { CurrentUserService } from '../currentUser.service';
import { CartService } from '../cart.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent {
    @Input() product?: Product;

    constructor(
        private route: ActivatedRoute,
        private productService: ProductService,
        private location: Location,
        private currentUserService: CurrentUserService,
        private cartService: CartService
      ) {}

    ngOnInit(): void {
        this.getProduct();
    }
      
    getProduct(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
        this.productService.getProduct(id)
          .subscribe(product => this.product = product);
    }

    goBack(): void {
        this.location.back();
    }

    addToCart(product: Product): void {
      this.currentUserService.getCurrentUser().subscribe((user) => {
            this.cartService.addProductToCart(user.id,product).subscribe();
            this.ngOnInit();
      });
  }

    save(): void {
        if (this.product) {
          this.productService.updateProduct(this.product)
            .subscribe(() => this.goBack());
        }
      }

}
