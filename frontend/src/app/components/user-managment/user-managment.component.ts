import { Component, OnInit } from '@angular/core';
import { UserRegistrationDTO} from "../../dtos/app-user";
import {NgClass, NgForOf,NgIf} from "@angular/common";
import {UserRole} from "../../dtos/app-user";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {AuthService} from "../../services/auth.service";

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
  changeWhat: UserRegistrationDTO;
  doWhat='?';
  whatRole:string;

  unconfirmedAdmins: UserRegistrationDTO[] = [
    {
      firstName: "Alice",
      lastName: "Johnson",
      email: "alice@example.com",
      mobileNumber: "123456789000000000000000000000000000000000000000000000000000000000 0",
      password: "password123",
      role: UserRole.UNCONFIRMED_ADMIN
    }
  ];
  unconfirmedEmployees: UserRegistrationDTO[] = [
    {
      firstName: "Bob",
      lastName: "Smith",
      email: "bob@example.com",
      mobileNumber: "0987654321",
      password: "password123",
      role: UserRole.UNCONFIRMED_EMPLOYEE
    }
  ];
  confirmedAdmins: UserRegistrationDTO[] = [
    {
      firstName: "Carol",
      lastName: "Taylor",
      email: "carol@example.com",
      mobileNumber: "1122334455",
      password: "password123",
      role: UserRole.ADMIN
    }
  ];
  confirmedEmployees: UserRegistrationDTO[] = [
    {
      firstName: "Dave",
      lastName: "Brown",
      email: "dave@example.com",
      mobileNumber: "2233445566",
      password: "password123",
      role: UserRole.EMPLOYEE
    }
  ];

  constructor(private authService: AuthService) {

  }

  ngOnInit(): void {
    this.fetchUsers();
  }

  fetchUsers(): void {
  }

  confirmUser(id: number): void {

  }

  changeUserRole(id: number, role: string): void {

  }

  deleteUser(id: number): void {

  }

  setChangeWhat(user:UserRegistrationDTO){
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
