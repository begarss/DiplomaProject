package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.domain.services.AuthService
import kz.kbtu.diplomaproject.domain.services.AuthServiceImpl
import kz.kbtu.diplomaproject.domain.services.UserService
import kz.kbtu.diplomaproject.domain.services.UserServiceImpl
import org.koin.dsl.module

val serviceModule = module {
  single<AuthService> { AuthServiceImpl(authApi = get(), preferences = get()) }
  single<UserService> { UserServiceImpl(preferences = get()) }
}