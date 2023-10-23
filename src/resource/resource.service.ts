import { ForbiddenException, Injectable, InternalServerErrorException, NotFoundException } from '@nestjs/common';
import { Role, User } from '@prisma/client';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateResourceDto } from './dto/create-resource.dto';
import { PatchResourceDto } from './dto/patch-resource.dto';
import { NotFoundError } from 'rxjs';

@Injectable()
export class ResourceService {
  constructor(private readonly prisma: PrismaService) {}

  async getAll() {
    const resources = await this.prisma.resource.findMany({
      include: {
        createdByUser: {
          select: {
            username: true,
          }
        }
      }
    });

    return resources;
  }

  async createResource(executingUser: User, dto: CreateResourceDto) {
    const resource = await this.prisma.resource.create({
      data: {
        createdByUserId: executingUser.id,
        ...dto
      }
    }) 

    if (!resource) {
      throw new InternalServerErrorException('Error when creating a resource');
    }

    return {
      message: 'success'
    };
  }

  async patchResource(executingUser: User, dto: PatchResourceDto, resourceId: number) {
    let resource = await this.prisma.resource.findUnique({
      where: {
        id: resourceId
      }
    });

    if (!resource) {
      throw new NotFoundException('There is no resource with such id');
    }

    if (executingUser.role == Role.CONTENT_MANAGER && resource.createdByUserId != executingUser.id) {
      throw new ForbiddenException('If you are content manager you can only manage your own resources!');
    }

    resource = await this.prisma.resource.update({
      where:{
        id: resourceId
      },
      data: {
        ...dto
      }
    });

    if (!resource) {
      throw new InternalServerErrorException('Error occured when patching resource!');
    }

    return {
      message: 'success'
    }
  }

  async deleteResource(resourceId: number) {
    const resourceExists = await this.prisma.resource.count({
      where: {
        id: resourceId
      }
    });

    if (!resourceExists)  {
      throw new NotFoundException('There is no resource with such id');
    }

    await this.prisma.resource.delete({
      where: {
        id: resourceId
      }
    });

    return {
      message: 'success'
    }
  }
}
