import { Transform } from 'class-transformer';
import { IsNotEmpty, IsBoolean, IsString } from 'class-validator';

export class CreateResourceDto {
  @IsNotEmpty()
  @IsString()
  name: string;

  @IsNotEmpty()
  @IsString()
  address: string;

  @IsNotEmpty()
  @IsString()
  attendingHours: string;

  @IsNotEmpty()
  @Transform(({ value }) => {
    return [true, 'enabled', 'true', 1, '1'].indexOf(value) > -1;
  })
  isActive: boolean;
}

