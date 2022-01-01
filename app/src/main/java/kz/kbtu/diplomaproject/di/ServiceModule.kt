package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.domain.services.AuthService
import kz.kbtu.diplomaproject.domain.services.AuthServiceImpl
import kz.kbtu.diplomaproject.domain.services.CompanyService
import kz.kbtu.diplomaproject.domain.services.CompanyServiceImpl
import kz.kbtu.diplomaproject.domain.services.DataBaseService
import kz.kbtu.diplomaproject.domain.services.DataBaseServiceImpl
import kz.kbtu.diplomaproject.domain.services.HomeService
import kz.kbtu.diplomaproject.domain.services.HomeServiceImpl
import kz.kbtu.diplomaproject.domain.services.OppService
import kz.kbtu.diplomaproject.domain.services.OppServiceImpl
import kz.kbtu.diplomaproject.domain.services.ProfileService
import kz.kbtu.diplomaproject.domain.services.ProfileServiceImpl
import kz.kbtu.diplomaproject.domain.services.UserService
import kz.kbtu.diplomaproject.domain.services.UserServiceImpl
import org.koin.dsl.module

val serviceModule = module {
  single<AuthService> { AuthServiceImpl(authApi = get(), preferences = get()) }
  single<UserService> { UserServiceImpl(preferences = get()) }
  single<HomeService> { HomeServiceImpl(homeApi = get()) }
  single<ProfileService> { ProfileServiceImpl(profileApi = get()) }
  single<CompanyService> { CompanyServiceImpl(companyApi = get()) }
  single<OppService> { OppServiceImpl(homeApi = get(), favDao = get()) }
  single<DataBaseService> { DataBaseServiceImpl(jobTypeDao = get(), contractDao = get()) }
}