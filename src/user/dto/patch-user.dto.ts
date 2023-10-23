import { Role } from "@prisma/client";
import { IsEnum, IsString } from "class-validator";

export class PatchUserDto {
  @IsString()
  username: string;

  @IsEnum(Role)
  role: Role;
}
