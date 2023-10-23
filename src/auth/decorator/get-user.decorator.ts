import { createParamDecorator } from '@nestjs/common';
import { User } from '@prisma/client';

export const GetUser = createParamDecorator(
    (_, ctx): User => {
        return ctx.switchToHttp().getRequest().user;
    }
);
