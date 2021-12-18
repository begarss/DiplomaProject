package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.domain.services.AuthService
import kz.kbtu.diplomaproject.domain.services.AuthServiceImpl
import org.koin.dsl.module

val serviceModule = module {
  single<AuthService> { AuthServiceImpl(authApi = get(), preferences = get()) }
}