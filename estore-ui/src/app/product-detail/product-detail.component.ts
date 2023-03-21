import { Component, Input } from '@angular/core';
import { Product } from '../product';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ProductService } from '../product.service';
import { CurrentUserService } from '../currentUser.service';
import { User } from '../user';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent {
    @Input() product?: Product;
    isAdmin: boolean = false;

    constructor(
        private route: ActivatedRoute,
        private productService: ProductService,
        private currentUserService: CurrentUserService,
        private location: Location
      ) {}

    ngOnInit(): void {
        this.getProduct();
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
      
    getProduct(): void {
        const id = Number(this.route.snapshot.paramMap.get('id'));
        this.productService.getProduct(id)
          .subscribe(product => this.product = product);
    }

    goBack(): void {
        this.location.back();
    }

    save(): void {
        if (this.product) {
          this.productService.updateProduct(this.product)
            .subscribe(() => this.goBack());
        }
      }

}
