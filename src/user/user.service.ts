import { ForbiddenException, Injectable, NotFoundException } from '@nestjs/common';
import { Role, User } from '@prisma/client';
import { PrismaService } from 'src/prisma/prisma.service';
import { PatchUserDto } from './dto/patch-user.dto';

@Injectable()
export class UserService {
  constructor(private readonly prisma: PrismaService) {}

  async getMe(user: User) {
    return user;
  }

  async getAll() {
    const users = await this.prisma.user.findMany({
      select: {
        id: true,
        username: true,
        role: true,
        updatedAt: true,
        createdAt: true,
      },
    });

    return users;
  }

  async patchUser(executingUser: User, dto: PatchUserDto, userId: number)  {
    if (executingUser.id === userId, dto?.role !== Role.SUPER_ADMINISTRATOR) {
      throw new ForbiddenException('You cannot change your role');
    }

    const userExists = await this.prisma.user.count({
      where: {
        id: userId
      }
    });

    if (!userExists) {
      throw new NotFoundException('User with this id does not exists!');
    }

    await this.prisma.user.update({
      where: {
        id: userId
      },
      data: {
        ...dto
      }
    });

    return {
      message: 'success'
    };
  }

  async deleteUser(executingUser: User, userId: number) {
    if (executingUser.id == userId) {
      throw new ForbiddenException('You cannot delete your own account!')
    }

    const userExists = await this.prisma.user.count({
      where: {
        id: userId
      }
    });

    if (!userExists) {
      throw new NotFoundException('User with this id does not exists!')
    }

    await this.prisma.user.delete({
      where: {
        id: userId
      }
    });

    return {
      message: 'success'
    };
  }
}
