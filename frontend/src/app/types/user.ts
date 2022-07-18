import {Role} from "./role";
import {Address} from "./address";

export class User {

  id?: number;
  publicId?: string;
  userName?: string;
  password?: string;
  isActive?: boolean;
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  birthDate?: Date;
  role?: Role;
  address?: Address;

  constructor(user: User = {}) {
    this.id = user.id;
    this.publicId = user.publicId;
    this.userName = user.userName;
    this.password = user.password;
    this.isActive = user.isActive;
    this.firstName = user.firstName;
    this.lastName = user.lastName;
    this.email = user.email;
    this.phone = user.phone;
    this.birthDate = user.birthDate;
    this.role = user.role;
    this.address = user.address;
  }

}
