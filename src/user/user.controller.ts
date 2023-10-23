import { Body, Controller, Delete, Get, Param, Patch, UseGuards } from '@nestjs/common';
import { Role, User } from '@prisma/client';
import { GetUser } from 'src/auth/decorator/get-user.decorator';
import { UserService } from './user.service';
import { JwtGuard } from 'src/auth/guard/jwt.guard';
import { Roles } from 'src/auth/decorator/roles.decorator';
import { RolesGuard } from 'src/auth/guard/roles.guard';
import { UserIdDto } from './dto/user-id.dto';
import { PatchUserDto } from './dto/patch-user.dto';

@Controller('user')
@UseGuards(JwtGuard)
export class UserController {
  constructor(private readonly userService: UserService) {}

  @Get('get-me')
  async getMe(@GetUser() user: User) {
    return this.userService.getMe(user);
  }

  @Roles(Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Get('get-all')
  async getAll() {
    return this.userService.getAll();
  }

  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Patch('set-self-info')
  async setSelfInfo() {
    //idk what informations for now so todo
  }

  @Roles(Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Patch(':id/patch')
  async patchUser(@Body() dto: PatchUserDto, @Param() {id}: UserIdDto, @GetUser() user: User) {
    return this.userService.patchUser(user, dto, id);
  }

  @Roles(Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Delete(':id/delete')
  async deleteUser(@GetUser() user: User, @Param() {id}: UserIdDto) {
    return this.userService.deleteUser(user, id);
  }
}
