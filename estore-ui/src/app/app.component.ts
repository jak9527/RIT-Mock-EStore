import { Component, OnInit } from '@angular/core';
import { CurrentUserService } from './currentUser.service';
import { Router } from '@angular/router';
import { UserUpdateService } from './userUpdate.service'
import { User } from './user';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
    private userSubscription: Subscription;
    currentUser: User = null as unknown as User;
    message: string = "";

  constructor(
    private currentUserService: CurrentUserService,
    private userUpdateService: UserUpdateService,
    private router: Router
  ) {
    this.userSubscription = this.userUpdateService.getUpdate().subscribe(message => {
        this.currentUserService.getCurrentUser().subscribe(curUser => this.currentUser = curUser)
        this.message = message;
    });

  }

  title = 'Rit Garage Sale';
  currentUserExists = false;

  ngOnInit(): void {
    this.currentUserTest();
  }
  
  currentUserTest(): void {
    this.currentUserService.getCurrentUser().subscribe((user) => {
      if( user != undefined && user.id != 0 ) {
        this.currentUserExists = true;
      }
      else {
        this.currentUserExists = false;
      }
    });
  }
  

  


}
