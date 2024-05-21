import {Component, OnInit} from '@angular/core';
import {UserOverviewDto, UserRole} from "../../dtos/app-user";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {ConfirmDialogComponent} from "../confirm-dialog/confirm-dialog.component";
import {AuthService} from "../../services/auth.service";
import {UserService} from "../../services/user.service";

@Component({
  selector: 'app-user-management',
  templateUrl: './staff-accounts.component.html',
  standalone: true,
  imports: [
    NgClass,
    NgForOf,
    NgIf,
    ConfirmDialogComponent
  ],
  styleUrls: ['./staff-accounts.component.scss']
})
export class StaffAccountsComponent implements OnInit {
  users: UserOverviewDto[] = [];
  unconfirmedAdmins: UserOverviewDto[] = [];
  unconfirmedEmployees: UserOverviewDto[] = [];
  confirmedAdmins: UserOverviewDto[] = [];
  confirmedEmployees: UserOverviewDto[] = [];
  changeWhat: UserOverviewDto;
  doWhat = '?';
  yesWhat = '?';
  whatRole: string;

  constructor(private authService: AuthService, private userService: UserService) {
  }

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

  confirmUser(user: UserOverviewDto): void {
    let userRole = user.role;
    if (userRole === UserRole.UNCONFIRMED_ADMIN) {
      user.role = UserRole.ADMIN;
    } else if (userRole === UserRole.UNCONFIRMED_EMPLOYEE) {
      user.role = UserRole.EMPLOYEE;
    }
    this.userService.updateUser(user).subscribe({
      next: (updatedUser) => {
        console.log('User confirmed successfully', updatedUser);
        this.fetchUsers();
      },
      error: (error) => {
        console.error('Failed to update user role', error);
      }
    });
  }

  changeUserRole(user: UserOverviewDto): void {
    let userRole = user.role;
    if (userRole === UserRole.ADMIN) {
      user.role = UserRole.EMPLOYEE;
    } else if (userRole === UserRole.EMPLOYEE) {
      user.role = UserRole.ADMIN;
    }
    this.userService.updateUser(user).subscribe({
      next: (updatedUser) => {
        console.log('User role updated successfully', updatedUser);
        this.fetchUsers();
      },
      error: (error) => {
        console.error('Failed to update user role', error);
      }
    });

  }

  deleteUser(id: number): void {
    // Implementation needed
  }

  setChangeWhat(user: UserOverviewDto) {
    this.changeWhat = user;
  }

  setDoWhat(action: number) {
    switch (action) {
      case 0:
        this.doWhat = "delete";
        this.yesWhat = "delete";
        this.whatRole = "?";

        break
      case 1:
        this.doWhat = "change the role of";
        this.yesWhat = "change role"
        if (this.changeWhat.role == "ADMIN") {
          this.whatRole = "from Admin to Employee?"
        } else if (this.changeWhat.role == "EMPLOYEE") {
          this.whatRole = "from Employee to Admin?"
        }
        break;
      case 2:
        this.doWhat = "confirm";
        this.yesWhat = 'confirm';
        if (this.changeWhat.role == "UNCONFIRMED_ADMIN") {
          this.whatRole = "as Admin?"
        } else if (this.changeWhat.role == "UNCONFIRMED_EMPLOYEE") {
          this.whatRole = "as Employee?"
        }
        break;
      default:
        this.doWhat = '?';
        this.yesWhat = '?';
    }
  }


  makeAction(action: String, user: UserOverviewDto) {
    if (action === "change the role of") {
      this.changeUserRole(user);
    }
    if (action === "confirm") {
      this.confirmUser(user);
    }
  }
}
