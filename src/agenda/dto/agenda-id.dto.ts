import { IsNotEmpty, IsNumber } from "class-validator";

export class AgendaIdDto {
  @IsNumber()
  @IsNotEmpty()
  id: number
}
