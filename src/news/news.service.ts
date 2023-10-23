import { ForbiddenException, Injectable, InternalServerErrorException, NotFoundException } from '@nestjs/common';
import { Role, User } from '@prisma/client';
import { PrismaService } from 'src/prisma/prisma.service';
import { CreateNewsDto } from './dto/create-news.dto';
import { PatchNewsDto } from './dto/patch-news.dto';

@Injectable()
export class NewsService {
  constructor(private readonly prisma: PrismaService) {}

  async getAll() {
    const news = await this.prisma.news.findMany({
      include: {
        createdByUser: {
          select: {
            username: true,
          }
        }
      }
    });

    return news;
  }

  async createNews(executingUser: User, dto: CreateNewsDto) {
    const news = await this.prisma.news.create({
      data: {
        createdByUserId: executingUser.id,
        ...dto
      }
    }) 

    if (!news) {
      throw new InternalServerErrorException('Error when creating a news');
    }

    return {
      message: 'success'
    };
  }

  async patchNews(executingUser: User, dto: PatchNewsDto, newsId: number) {
    let news = await this.prisma.news.findUnique({
      where: {
        id: newsId
      }
    });

    if (!newsId) {
      throw new NotFoundException('There is no news with such id');
    }

    if (executingUser.role == Role.CONTENT_MANAGER && news.createdByUserId != executingUser.id) {
      throw new ForbiddenException('If you are content manager you can only manage your own news!');
    }

    news = await this.prisma.news.update({
      where:{
        id: newsId
      },
      data: {
        ...dto
      }
    });

    if (!news) {
      throw new InternalServerErrorException('Error occured when patching news!');
    }

    return {
      message: 'success'
    }
  }

  async deleteNews(newsId: number) {
    const newsExists = await this.prisma.news.count({
      where: {
        id: newsId
      }
    });

    if (!newsExists)  {
      throw new NotFoundException('There is no news with such id');
    }

    await this.prisma.news.delete({
      where: {
        id: newsId
      }
    });

    return {
      message: 'success'
    }
  }
}
