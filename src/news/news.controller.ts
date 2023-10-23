import { Body, Controller, Delete, Get, Param, Patch, Post, UseGuards } from '@nestjs/common';
import { Role, User } from '@prisma/client';
import { GetUser } from 'src/auth/decorator/get-user.decorator';
import { Roles } from 'src/auth/decorator/roles.decorator';
import { RolesGuard } from 'src/auth/guard/roles.guard';
import { NewsService } from './news.service';
import { CreateNewsDto } from './dto/create-news.dto';
import { PatchNewsDto } from './dto/patch-news.dto';
import { NewsIdDto } from './dto/news-id.dto';
import { JwtGuard } from 'src/auth/guard/jwt.guard';

@Controller('news')
@UseGuards(JwtGuard)
export class NewsController {
  constructor(private readonly newsService: NewsService) {}

  @Get('get-all')
  async getAll() {
    return this.newsService.getAll();
  } 

  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Post('create')
  async createNews(@GetUser() user: User, @Body() dto: CreateNewsDto) {
    return this.newsService.createNews(user, dto);
  }

  @Roles(Role.CONTENT_MANAGER, Role.ADMINISTRATOR, Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Patch(':id/patch')
  async patchNews(@GetUser() user: User, @Body() dto: PatchNewsDto, @Param() {id}: NewsIdDto) {
    return this.newsService.patchNews(user, dto, id);
  }

  @Roles(Role.SUPER_ADMINISTRATOR)
  @UseGuards(RolesGuard)
  @Delete(':id/delete')
  async deleteNews(@Param() {id}: NewsIdDto) {
    return this.newsService.deleteNews(id);
  }

}
