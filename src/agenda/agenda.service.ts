import { ForbiddenException, Injectable, InternalServerErrorException, NotFoundException } from '@nestjs/common';
import { Role, User } from '@prisma/client';
import { PrismaService } from 'src/prisma/prisma.service';
import { PatchAgendaDto } from './dto/patch-agenda.dto';
import { CreateAgendaDto } from './dto/create-agenda.dto';

@Injectable()
export class AgendaService {
  constructor(private readonly prisma: PrismaService) {}

  async getAll() {
    const agendas = await this.prisma.agenda.findMany({
      include: {
        createdByUser: {
          select: {
            username: true,
          }
        }
      }
    });

    return agendas;
  }

  async createAgenda(executingUser: User, dto: CreateAgendaDto) {
    const date = new Date(dto.date)

    const agenda = await this.prisma.agenda.create({
      data: {
        createdByUserId: executingUser.id,
        date,
        ...dto
      }
    });

    if (!agenda) {
      throw new InternalServerErrorException('Error when creating a agenda');
    }

    return {
      message: 'success'
    };
  }

  async patchAgenda(executingUser: User, dto: PatchAgendaDto, agendaId: number) {
    let agenda = await this.prisma.agenda.findUnique({
      where: {
        id: agendaId
      }
    });

    if (!agenda) {
      throw new NotFoundException('There is no agenda with such id');
    }

    if (executingUser.role == Role.CONTENT_MANAGER && agenda.createdByUserId != executingUser.id) {
      throw new ForbiddenException('If you are content manager you can only manage your own agendas!');
    }

    const date = new Date(dto?.date ? dto?.date : agenda.date)

    agenda = await this.prisma.agenda.update({
      where:{
        id: agendaId
      },
      data: {
        date,
        ...dto
      }
    });

    if (!agenda) {
      throw new InternalServerErrorException('Error occured when patching agenda!');
    }

    return {
      message: 'success'
    }
  }

  async deleteAgenda(agendaId: number) {
    const agendaExists = await this.prisma.agenda.count({
      where: {
        id: agendaId
      }
    });

    if (!agendaExists)  {
      throw new NotFoundException('There is no agenda with such id');
    }

    await this.prisma.agenda.delete({
      where: {
        id: agendaId
      }
    });

    return {
      message: 'success'
    }
  }

}
