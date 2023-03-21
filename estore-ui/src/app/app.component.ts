import { Component, OnInit, OnChanges } from '@angular/core';
import { Router } from '@angular/router';
import { UserUpdateService } from './userUpdate.service'
import { CurrentUserService } from './currentUser.service';
import { User } from './user';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
    private userSubscription: Subscription;
    currentUser: User = this.initUser();
    message: string = "";
    isAdmin: boolean = false;

  constructor(
    private currentUserService: CurrentUserService,
    private userUpdateService: UserUpdateService,
    private router: Router
  ) {
    this.updateHeader;
    this.userSubscription = this.userUpdateService.getUpdate().subscribe(message => {
            this.updateHeader();
            this.message = message;
    });

  }

  title = 'Rit Garage Sale';
  currentUserExists = false;

  ngOnInit(): void {
    this.updateHeader();
  }

  ngOnChanges(): void {
    
  }

  initUser(): User {
    var theUser: User = null as unknown as User;
    this.currentUserService.getCurrentUser().subscribe(curUser => theUser = curUser);
    return theUser;
  }

  updateHeader(): void {
    this.currentUserService.getCurrentUser().subscribe(curUser =>{
        this.currentUser = curUser;
        if (this.currentUser.id == 0){
            this.isAdmin = true;
        } else {
            this.isAdmin = false;
        }
    });
  }

}
