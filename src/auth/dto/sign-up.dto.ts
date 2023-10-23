import { IsNotEmpty, Length, MinLength } from "class-validator";

export class SignUpDto {
  @IsNotEmpty()
  @Length(4, 20)
  username: string

  @IsNotEmpty()
  @Length(8, 50)
  password: string
}
