import { IsNotEmpty, IsInt, IsString, IsBoolean, IsDate, IsDateString } from 'class-validator';
import { Transform, Type } from 'class-transformer';

export class PatchAgendaDto {
  @IsString()
  activity: string;

  @IsString()
  description: string;

  @IsString()
  address: string;

  @IsDateString()
  date: string;

  @Transform(({ value }) => {
    return [true, 'enabled', 'true', 1, '1'].indexOf(value) > -1;
  })
  active: boolean;
}

