import { Injectable } from '@angular/core';

import { User } from './user';

import { Observable, of } from 'rxjs';

import { MessageService } from './message.service';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

    private usersUrl = 'http://localhost:8080/currentUser'

    constructor(
        private http: HttpClient,
        private messageService: MessageService) { }



    /** GET product current user. Will 404 if id not found */
    getCurrentUser(): Observable<User> {
        return this.http.get<User>(this.usersUrl).pipe(
            tap(_ => this.log(`fetched current user`)),
            catchError(this.handleError<User>(`getCurrentUser`))
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

    /** POST: add a new user to the server */
    setCurrentUser(user: User): Observable<User> {
      return this.http.post<User>(this.usersUrl, user, this.httpOptions).pipe(
        tap((newUser: User) => this.log(`set user w/ id=${newUser.id}`)),
        catchError(this.handleError<User>('setCurrentUser'))
      );
    }

}

