export class User {
  private _id: string;
  private _firstName: string;
  private _lastName:string;
  private _username: string;
  private _email: string;
  private _createdAt: Date;
  private _updatedAt: Date;


  constructor(id: string, firstName: string, lastName: string, username: string, email: string, createdAt: Date, updatedAt: Date) {
    this._id = id;
    this._firstName = firstName;
    this._lastName = lastName;
    this._username = username;
    this._email = email;
    this._createdAt = createdAt;
    this._updatedAt = updatedAt;
  }


  get id(): string {
    return this._id;
  }

  set id(value: string) {
    this._id = value;
  }

  get firstName(): string {
    return this._firstName;
  }

  set firstName(value: string) {
    this._firstName = value;
  }

  get lastName(): string {
    return this._lastName;
  }

  set lastName(value: string) {
    this._lastName = value;
  }

  get username(): string {
    return this._username;
  }

  set username(value: string) {
    this._username = value;
  }

  get email(): string {
    return this._email;
  }

  set email(value: string) {
    this._email = value;
  }

  get createdAt(): Date {
    return this._createdAt;
  }

  set createdAt(value: Date) {
    this._createdAt = value;
  }

  get updatedAt(): Date {
    return this._updatedAt;
  }

  set updatedAt(value: Date) {
    this._updatedAt = value;
  }
}
