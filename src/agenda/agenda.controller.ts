import { Body, Controller, Delete, Get, Param, Patch, Post, UseGuards } from '@nestjs/common';
import { JwtGuard } from 'src/auth/guard/jwt.guard';
import { AgendaService } from './agenda.service';
import { Roles } from 'src/auth/decorator/roles.decorator';
import { GetUser } from 'src/auth/decorator/get-user.decorator';
import { Role, User } from '@prisma/client';
import { RolesGuard } from 'src/auth/guard/roles.guard';
import { CreateAgendaDto } from './dto/create-agenda.dto';
import { PatchAgendaDto } from './dto/patch-agenda.dto';
import { AgendaIdDto } from './dto/agenda-id.dto';

@Controller('agenda')
export class AgendaController {
  constructor(private readonly agendaService: AgendaService) {}

  @Get('get-active')
  async getActive() {
    return this.agendaService.getActive();
  } 

  @UseGuards(JwtGuard, RolesGuard)
  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @Get('get-all')
  async getAll() {
    return this.agendaService.getAll();
  } 


  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
@UseGuards(JwtGuard)
  @Post('create')
  async createAgenda(@GetUser() user: User, @Body() dto: CreateAgendaDto) {
    return this.agendaService.createAgenda(user, dto);
  }

  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
@UseGuards(JwtGuard)
  @Patch(':id/patch')
  async patchAgenda(@GetUser() user: User, @Body() dto: PatchAgendaDto, @Param() {id}: AgendaIdDto) {
    return this.agendaService.patchAgenda(user, dto, id);
  }

  @Roles(Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
@UseGuards(JwtGuard)
  @Delete(':id/delete')
  async deleteAgenda(@Param() {id}: AgendaIdDto) {
    return this.agendaService.deleteAgenda(id);
  }


}
