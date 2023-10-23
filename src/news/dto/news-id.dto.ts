import { IsNotEmpty, IsNumber } from "class-validator";

export class NewsIdDto {
  @IsNumber()
  @IsNotEmpty()
  id: number
}
