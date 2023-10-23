import { IsNotEmpty, IsNumber } from "class-validator";

export class ResourceIdDto {
  @IsNumber()
  @IsNotEmpty()
  id: number
}
