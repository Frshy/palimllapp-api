import { ConflictException, ForbiddenException, Injectable } from '@nestjs/common';
import { SignUpDto } from './dto/sign-up.dto';
import { PrismaService } from 'src/prisma/prisma.service';
import * as bcrypt from 'bcrypt'
import { SignInDto } from './dto/sign-in.dto';
import { JwtService } from '@nestjs/jwt';

@Injectable()
export class AuthService {
  constructor(
    private readonly prisma: PrismaService,
    private readonly jwtService: JwtService
  ) {}

  async signUp(dto: SignUpDto) {
    const usernameTaken = await this.prisma.user.count({
      where: {
        username: dto.username
      }
    })
  
    if (usernameTaken) {
      throw new ConflictException('Username is already taken!')
    }

    const salt = await bcrypt.genSalt();
    const passwordHash = bcrypt.hashSync(dto.password, salt)

    const user = await this.prisma.user.create({
      data: {
        username: dto.username,
        passwordHash
      }
    })

    const payload = { id: user.id }

    return {
      access_token: this.jwtService.sign(payload)
    }
  }

  async signIn(dto: SignInDto) {
    const user = await this.prisma.user.findUnique({
      where: {
        username: dto.username
      }
    })

    if (!user) {
      throw new ForbiddenException('User with this username does not exists!')
    }

    const passwordsMatch = await bcrypt.compare(dto.password, user.passwordHash)

    if (!passwordsMatch) {
      throw new ForbiddenException(
        'Incorrect password!'
      )
    }

    const payload = { id: user.id }

    return {
      access_token: this.jwtService.sign(payload)
    }
  }
}
