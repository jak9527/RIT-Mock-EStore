import { Component, OnInit } from '@angular/core';
import { CurrentUserService } from './currentUser.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  constructor(
    private currentUserService: CurrentUserService,
    private router: Router
  ) {}

  title = 'Rit Garage Sale';
  currentUser = this.currentUserService.getCurrentUser;
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
