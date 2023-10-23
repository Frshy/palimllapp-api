import { IsNotEmpty, IsNumber } from "class-validator";

export class UserIdDto {
  @IsNumber()
  @IsNotEmpty()
  id: number
}
