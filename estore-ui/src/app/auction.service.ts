import { Injectable } from '@angular/core';

import { Auction } from './auction';

import { Observable, of } from 'rxjs';

import { MessageService } from './message.service';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Product } from './product';

@Injectable({
  providedIn: 'root'
})
export class AuctionService {

    private auctionUrl = 'http://localhost:8080/auction'

    constructor(
        private http: HttpClient,
        private messageService: MessageService) { }


    /** GET auction. Will 404 if id not found */
    getAuction(): Observable<Auction> {
        const url = `${this.auctionUrl}`;
        return this.http.get<Auction>(url).pipe(
            tap(_ => this.log(`fetched auction`)),
            catchError(this.handleError<Auction>(`getCurrentAuction`))
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

    /** Log a AuctionService message with the MessageService */
    private log(message: string) {
        this.messageService.add(`AuctionService: ${message}`);
    }

    /** PUT: update the auction on the server */
    updateAuction(username: string, bid: number, endDate: string, product: Product): Observable<any> {
        const url = `${this.auctionUrl}/${username}/${bid}/${endDate} ${product}`;
        return this.http.put(this.auctionUrl, product, this.httpOptions).pipe(
        tap(_ => this.log(`updated auction`)),
        catchError(this.handleError<any>('updateAuction'))
        );
    }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
      };

    /** POST: add a new auction to the server */
    addAuction(id: number, username: string, bid: number, endDate: string, product: Product): Observable<Auction> {
        const url = `${this.auctionUrl}/${id}/${username}/${bid}/${endDate} ${product}`;
        return this.http.post<Auction>(this.auctionUrl, product, this.httpOptions).pipe(
        tap((newAuction: Auction) => this.log(`added auction w/ id=${newAuction.id}`)),
        catchError(this.handleError<Auction>('addAuction'))
        );
    }

    /** DELETE: delete the auction from the server */
    deleteAuction(): Observable<Auction> {
        return this.http.delete<Auction>(this.auctionUrl, this.httpOptions).pipe(
        tap(_ => this.log(`deleted auction`)),
        catchError(this.handleError<Auction>('deleteAuction'))
        );
    }

    /** PUT: update the auction on the server */
    newBid(username: string, bid: number): Observable<any> {
        const url = `${this.auctionUrl}/${username}/${bid}`;
        return this.http.put(this.auctionUrl, null, this.httpOptions).pipe(
        tap(_ => this.log(`updated bid`)),
        catchError(this.handleError<any>('newBid'))
        );
    }

    /* GET boolean for if auction is over */
    getAuctionStatus(): Observable<Boolean> {
        const url = `${this.auctionUrl}/1`;
        return this.http.get<Boolean>(url).pipe(
            tap(_ => this.log(`fetched auction status`)),
            catchError(this.handleError<Boolean>(`getCurrentAuctionStatus`))
        );
    }

}

