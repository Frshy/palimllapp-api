import { Transform } from 'class-transformer';
import { IsNotEmpty, IsBoolean, IsString } from 'class-validator';

export class PatchResourceDto {
  @IsString()
  name: string;

  @IsString()
  address: string;

  @IsString()
  attendingHours: string;

  @Transform(({ value }) => {
    return [true, 'enabled', 'true', 1, '1'].indexOf(value) > -1;
  })
  isActive: boolean;
}

