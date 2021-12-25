package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.AuthInteractor
import kz.kbtu.diplomaproject.presentation.auth.AuthInteractorImpl
import kz.kbtu.diplomaproject.presentation.base.UserInteractor
import kz.kbtu.diplomaproject.presentation.base.UserInteractorImpl
import kz.kbtu.diplomaproject.presentation.company.CompanyInteractor
import kz.kbtu.diplomaproject.presentation.company.CompanyInteractorImpl
import kz.kbtu.diplomaproject.presentation.favourites.OppInteractor
import kz.kbtu.diplomaproject.presentation.favourites.OppInteractorImpl
import kz.kbtu.diplomaproject.presentation.home.HomeInteractor
import kz.kbtu.diplomaproject.presentation.home.HomeInteractorImpl
import kz.kbtu.diplomaproject.presentation.profile.ProfileInteractor
import kz.kbtu.diplomaproject.presentation.profile.ProfileInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
  single<AuthInteractor> { AuthInteractorImpl(authService = get()) }
  single<UserInteractor> { UserInteractorImpl(userService = get()) }
  single<HomeInteractor> { HomeInteractorImpl(homeService = get()) }
  single<ProfileInteractor> { ProfileInteractorImpl(profileService = get()) }
  single<CompanyInteractor> { CompanyInteractorImpl(companyService = get()) }
  single<OppInteractor> { OppInteractorImpl(oppService = get()) }
}