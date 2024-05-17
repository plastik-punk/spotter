import { Component, OnInit } from '@angular/core';
import {UserOverviewDto, UserRegistrationDTO} from "../../dtos/app-user";
import {NgClass, NgForOf,NgIf} from "@angular/common";
import {UserRole} from "../../dtos/app-user";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-managment.component.html',
  standalone: true,
  imports: [
    NgClass,
    NgForOf,
    NgIf,
    ConfirmDialogComponent
  ],
  styleUrls: ['./user-managment.component.scss']
})
export class UserManagementComponent implements OnInit {
  users: UserOverviewDto[] = [];
  unconfirmedAdmins: UserOverviewDto[] = [];
  unconfirmedEmployees: UserOverviewDto[] = [];
  confirmedAdmins: UserOverviewDto[] = [];
  confirmedEmployees: UserOverviewDto[] = [];
  changeWhat: UserOverviewDto;
  doWhat='?';
  whatRole:string;

  constructor(private authService: AuthService, private userService: UserService) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
    this.userService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.categorizeUsers();
      },
      error: (error) => {
        console.error('Failed to fetch users', error);
      }
    });
  }

  categorizeUsers(): void {
    this.unconfirmedAdmins = this.users.filter(user => user.role === 'UNCONFIRMED_ADMIN');
    this.unconfirmedEmployees = this.users.filter(user => user.role === 'UNCONFIRMED_EMPLOYEE');
    this.confirmedAdmins = this.users.filter(user => user.role === 'ADMIN');
    this.confirmedEmployees = this.users.filter(user => user.role === 'EMPLOYEE');
  }

  confirmUser(id: number): void {
    // Implementation needed
  }

  changeUserRole(id: number, role: string): void {
    // Implementation needed
  }

  deleteUser(id: number): void {
    // Implementation needed
  }

  setChangeWhat(user:UserOverviewDto){
this.changeWhat=user;
  }

  setDoWhat(action:number){
    switch (action){
      case 0:
        this.doWhat="delete";
        this.whatRole="?";

      break
      case 1: this.doWhat = "change the role of";
        if (this.changeWhat.role=="ADMIN"){
          this.whatRole="from Admin to Employee?"
        }else if(this.changeWhat.role=="EMPLOYEE"){
          this.whatRole="from Employee to Admin?"
        }
      break;
      case 2:
        this.doWhat = "confirm";
        if (this.changeWhat.role=="UNCONFIRMED_ADMIN"){
          this.whatRole="as Admin?"
        }else if(this.changeWhat.role=="UNCONFIRMED_EMPLOYEE"){
          this.whatRole="as Employee?"
        }
      break;
      default: this.doWhat='?';
    }
  }
}
