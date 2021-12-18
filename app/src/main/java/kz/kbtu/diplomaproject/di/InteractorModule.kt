package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.AuthInteractor
import kz.kbtu.diplomaproject.presentation.auth.AuthInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
  single<AuthInteractor> { AuthInteractorImpl(authService = get()) }
}