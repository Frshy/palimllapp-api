import { Body, Controller, Delete, Get, Param, Patch, Post, UseGuards } from '@nestjs/common';
import { ResourceService } from './resource.service';
import { JwtGuard } from 'src/auth/guard/jwt.guard';
import { RolesGuard } from 'src/auth/guard/roles.guard';
import { Roles } from 'src/auth/decorator/roles.decorator';
import { Role, User } from '@prisma/client';
import { GetUser } from 'src/auth/decorator/get-user.decorator';
import { CreateResourceDto } from './dto/create-resource.dto';
import { PatchResourceDto } from './dto/patch-resource.dto';
import { ResourceIdDto } from './dto/resource-id.dto';

@Controller('resource')
export class ResourceController {
  constructor(private readonly resourceService: ResourceService) { }

  @Get('get-active')
  async getActive() {
    return this.resourceService.getActive();
  } 

  @UseGuards(JwtGuard, RolesGuard)
  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @Get('get-all')
  async getAll() {
    return this.resourceService.getAll();
  } 

  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
@UseGuards(JwtGuard)
  @Post('create')
  async createResource(@GetUser() user: User, @Body() dto: CreateResourceDto) {
    return this.resourceService.createResource(user, dto);
  }

  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
@UseGuards(JwtGuard)
  @Patch(':id/patch')
  async patchResource(@GetUser() user: User, @Body() dto: PatchResourceDto, @Param() {id}: ResourceIdDto) {
    return this.resourceService.patchResource(user, dto, id);
  }

  @Roles(Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
@UseGuards(JwtGuard)
  @Delete(':id/delete')
  async deleteResource(@Param() {id}: ResourceIdDto) {
    return this.resourceService.deleteResource(id);
  }
}
