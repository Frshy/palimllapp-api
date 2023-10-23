import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { AuthModule } from './auth/auth.module';
import { UserModule } from './user/user.module';
import { ResourceModule } from './resource/resource.module';
import { NewsModule } from './news/news.module';
import { AgendaModule } from './agenda/agenda.module';

@Module({
  imports: [AuthModule, UserModule, ResourceModule, NewsModule, AgendaModule],
  controllers: [AppController],
  providers: [AppService],
})
export class AppModule {}
