import { Component, OnInit } from '@angular/core';

import { User } from '../user';
import { UserService } from '../user.service';
import { CurrentUserService } from '../currentUser.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user: User = null as unknown as User;

  constructor(private userService: UserService,
    private currentUserService: CurrentUserService) { }

  ngOnInit(): void {
  }

  getUser(name: string): void {
    this.userService.getUser(name)
    .subscribe(newuser => this.user = newuser);
  }

  add(name: string): void {
    name = name.trim();
    if (!name) { return; }
    this.userService.getUser(name)
    .subscribe(newuser => { 
        this.user = newuser
        if (this.user === null){
            this.userService.addUser({"id":0,"username":name} as User)
              .subscribe(newuser => {
                this.user = newuser;
              });
        }
        this.currentUserService.setCurrentUser(this.user).subscribe();
    });
    
    
    
    

    
    
  }

  logout(): void {
    this.user = null as unknown as User;
    this.currentUserService.deleteCurrentUser().subscribe();
  }

}