import { Transform } from 'class-transformer';
import { IsNotEmpty, IsBoolean, IsString } from 'class-validator';

export class PatchNewsDto {
  @IsString()
  title: string;

  @IsString()
  content: string;

  @Transform(({ value }) => {
    return [true, 'enabled', 'true', 1, '1'].indexOf(value) > -1;
  })
  active: boolean;
}

