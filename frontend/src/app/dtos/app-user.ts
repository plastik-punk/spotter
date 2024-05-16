export interface AppUser {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  username: string;
  mobileNumber: string;
  password: string;
  role: number;
}

export interface UserRegistrationDTO {
  firstName: string;
  lastName: string;
  email: string;
  mobileNumber?: string;
  password: string;
  role: UserRole;
}

export enum UserRole {
  ADMIN = 'ADMIN',
  EMPLOYEE = 'EMPLOYEE',
  CUSTOMER = 'CUSTOMER',
  GUEST = 'GUEST',
  UNCONFIRMED_ADMIN = 'UNCONFIRMED_ADMIN',
  UNCONFIRMED_EMPLOYEE = 'UNCONFIRMED_EMPLOYEE'

}

