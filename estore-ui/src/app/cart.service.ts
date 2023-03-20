import { Injectable } from '@angular/core';

import { Cart } from './cart';
import { Product } from './product';

import { Observable, of } from 'rxjs';

import { MessageService } from './message.service';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CartService {

    private cartsUrl = 'http://localhost:8080/carts'

    constructor(
        private http: HttpClient,
        private messageService: MessageService) { }



    /** GET cart by id. Will 404 if id not found */
    getCart(id: number): Observable<Cart> {
        const url = `${this.cartsUrl}/${id}`;
        return this.http.get<Cart>(url).pipe(
            tap(_ => this.log(`fetched cart id=${id}`)),
            catchError(this.handleError<Cart>(`getCart id=${id}`))
        );
    }

    /**
    * Handle Http operation that failed.
    * Let the app continue.
    *
     * @param operation - name of the operation that failed
    * @param result - optional value to return as the observable result
     */
    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
  
             // TODO: send the error to remote logging infrastructure
             console.error(error); // log to console instead
  
             // TODO: better job of transforming error for user consumption
            this.log(`${operation} failed: ${error.message}`);
  
            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }

    /** Log a UserService message with the MessageService */
    private log(message: string) {
        this.messageService.add(`UserService: ${message}`);
    }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };

    /** POST: add a new cart to the server */
    addCart(cart: Cart): Observable<Cart> {
      return this.http.post<Cart>(this.cartsUrl, cart, this.httpOptions).pipe(
        tap((newCart: Cart) => this.log(`added cart w/ id=${newCart.id}`)),
        catchError(this.handleError<Cart>('addCart'))
      );
    }

    /** PUT: add a product to a cart on the server */
    addProductToCart(cId: number, product: Product): Observable<Cart> {
        return this.http.put<Cart>(this.cartsUrl+`/${cId}`, product, this.httpOptions).pipe(
            tap(_ => this.log(`added product with id ${product.id} to cart w/ id=${cId}`)),
            catchError(this.handleError<Cart>('addProductToCart'))
          );
    }

    /** PUT: increment a product by a count in a cart on the server */
    updateProductCount(cId: number, pId: number, count: number): Observable<Cart> {
        return this.http.put<Cart>(this.cartsUrl+`/${cId}/${pId}/${count}`, null, this.httpOptions).pipe(
            tap(_ => this.log(`incremented product with id ${pId} in cart w/ id=${cId} by ${count}`)),
            catchError(this.handleError<Cart>('addProductToCart'))
          );
    }

}

