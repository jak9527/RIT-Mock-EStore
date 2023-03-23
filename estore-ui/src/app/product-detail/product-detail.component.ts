import { Component, Input } from '@angular/core';
import { Product } from '../product';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ProductService } from '../product.service';
import { CurrentUserService } from '../currentUser.service';
import { User } from '../user';
import { CartService } from '../cart.service';

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
        private location: Location,
        private cartService: CartService
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

    addToCart(product: Product): void {
      this.currentUserService.getCurrentUser().subscribe((user) => {
          this.cartService.getCart(user.id).subscribe((cart => {
            let what: string[] = Object.keys(cart.products)
            if( what.includes(product.id.toString())) {
              this.cartService.updateProductCount(user.id, product.id, 1).subscribe();
              this.ngOnInit();
            }
            else {
              this.cartService.addProductToCart(user.id, product).subscribe();
              this.ngOnInit(); 
            }
            }))
      });
      var temp = document.getElementsByTagName("template")[0];
      var clon = temp.content.cloneNode(true);
      document.body.appendChild(clon);
      setTimeout(function(){
        var old = document.getElementById("alert");
        document.body.removeChild(old as Node);
      }, 3000);
    }

    save(): void {
        if (this.product) {
          this.productService.updateProduct(this.product)
            .subscribe(() => this.goBack());
        }
      }

}
