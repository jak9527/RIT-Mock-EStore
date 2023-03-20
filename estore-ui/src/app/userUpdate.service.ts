import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { User } from './user';
    
@Injectable({ providedIn: 'root' })
export class UserUpdateService {
    private subjectName = new Subject<any>(); //need to create a subject
    
    sendUpdate(user: User) { //the component that wants to update something, calls this fn
        this.subjectName.next({ user: user }); //next() will feed the value in Subject
    }
    
    getUpdate(): Observable<any> { //the receiver component calls this function 
        return this.subjectName.asObservable(); //it returns as an observable to which the receiver funtion will subscribe
    }
}