package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.AuthInteractor
import kz.kbtu.diplomaproject.presentation.auth.AuthInteractorImpl
import kz.kbtu.diplomaproject.presentation.base.UserInteractor
import kz.kbtu.diplomaproject.presentation.base.UserInteractorImpl
import kz.kbtu.diplomaproject.presentation.home.HomeInteractor
import kz.kbtu.diplomaproject.presentation.home.HomeInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
  single<AuthInteractor> { AuthInteractorImpl(authService = get()) }
  single<UserInteractor> { UserInteractorImpl(userService = get()) }
  single<HomeInteractor> { HomeInteractorImpl(homeService = get()) }
}