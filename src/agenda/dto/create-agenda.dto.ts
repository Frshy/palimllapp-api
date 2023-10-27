import { IsNotEmpty, IsInt, IsString, IsBoolean, IsDate, IsDateString } from 'class-validator';
import { Transform, Type } from 'class-transformer';

export class CreateAgendaDto {
  @IsString()
  @IsNotEmpty()
  activity: string;

  @IsString()
  @IsNotEmpty()
  description: string;

  @IsString()
  @IsNotEmpty()
  address: string;

  @IsDateString()
  @IsNotEmpty()
  date: string;

  @IsNotEmpty()
  @Transform(({ value }) => {
    return [true, 'enabled', 'true', 1, '1'].indexOf(value) > -1;
  })
  active: boolean;
}

