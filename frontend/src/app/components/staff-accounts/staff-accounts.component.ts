import { Component, OnInit } from '@angular/core';
import { UserOverviewDto, UserRole } from "../../dtos/app-user";
import { NgClass, NgForOf, NgIf } from "@angular/common";
import { ConfirmDialogComponent } from "../confirm-dialog/confirm-dialog.component";
import { AuthService } from "../../services/auth.service";
import { UserService } from "../../services/user.service";
import { NotificationService } from "../../services/notification.service";

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
  confirmHeader = '?';
  doWhat = '?';
  yesWhat = '?';
  whatRole: string;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private notificationService: NotificationService
  ) { }

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
        this.notificationService.showError('Failed to fetch users. Please try again later.');
      }
    });
  }

  categorizeUsers(): void {
    this.unconfirmedAdmins = this.users
      .filter(user => user.role === 'UNCONFIRMED_ADMIN')
      .sort((a, b) => a.firstName.localeCompare(b.firstName));
    this.unconfirmedEmployees = this.users
      .filter(user => user.role === 'UNCONFIRMED_EMPLOYEE')
      .sort((a, b) => a.firstName.localeCompare(b.firstName));
    this.confirmedAdmins = this.users
      .filter(user => user.role === 'ADMIN')
      .sort((a, b) => a.firstName.localeCompare(b.firstName));
    this.confirmedEmployees = this.users
      .filter(user => user.role === 'EMPLOYEE')
      .sort((a, b) => a.firstName.localeCompare(b.firstName));
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
        this.notificationService.showSuccess('User confirmed successfully.');
        this.fetchUsers();
      },
      error: (error) => {
        this.notificationService.showError('Failed to update user role. Please try again later.');
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
        this.notificationService.showSuccess('User role updated successfully.');
        this.fetchUsers();
      },
      error: (error) => {
        this.notificationService.showError('Failed to update user role. Please try again later.');
      }
    });
  }

  deleteUser(user: UserOverviewDto): void {
    this.userService.deleteUser(user.id).subscribe({
      next: (deletedUser) => {
        this.notificationService.showSuccess(`User ${user.firstName} ${user.lastName} deleted successfully.`);

        this.fetchUsers();
      },
      error: (error) => {
        this.notificationService.showError('Failed to delete user. Please try again later.');
      }
    });
  }

  setChangeWhat(user: UserOverviewDto) {
    this.changeWhat = user;
  }

  setDoWhat(action: number) {
    switch (action) {
      case 0:
        this.confirmHeader = "Delete";
        this.doWhat = "delete";
        this.yesWhat = "delete";
        this.whatRole = "?";
        break;
      case 1:
        this.confirmHeader = "Change role";
        this.doWhat = "change the role of";
        this.yesWhat = "change role";
        if (this.changeWhat.role == "ADMIN") {
          this.whatRole = "from Admin to Employee?";
        } else if (this.changeWhat.role == "EMPLOYEE") {
          this.whatRole = "from Employee to Admin?";
        }
        break;
      case 2:
        this.confirmHeader = "Confirm";
        this.doWhat = "confirm";
        this.yesWhat = 'confirm';
        if (this.changeWhat.role == "UNCONFIRMED_ADMIN") {
          this.whatRole = "as Admin?";
        } else if (this.changeWhat.role == "UNCONFIRMED_EMPLOYEE") {
          this.whatRole = "as Employee?";
        }
        break;
      default:
        this.confirmHeader = '?';
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
    if (action === "delete") {
      this.deleteUser(user);
    }
  }
}
