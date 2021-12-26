package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.AuthViewModel
import kz.kbtu.diplomaproject.presentation.company.CompanyViewModel
import kz.kbtu.diplomaproject.presentation.explore.SearchViewModel
import kz.kbtu.diplomaproject.presentation.explore.filter.FilterViewModel
import kz.kbtu.diplomaproject.presentation.favourites.FavouritesViewModel
import kz.kbtu.diplomaproject.presentation.home.DetailVIewModel
import kz.kbtu.diplomaproject.presentation.home.HomeViewModel
import kz.kbtu.diplomaproject.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { AuthViewModel(authInteractor = get()) }
  viewModel { HomeViewModel(homeInteractor = get(), oppInteractor = get()) }
  viewModel { ProfileViewModel(profileInteractor = get()) }
  viewModel { DetailVIewModel(homeInteractor = get(), oppInteractor = get()) }
  viewModel { SearchViewModel(homeInteractor = get()) }
  viewModel { CompanyViewModel(companyInteractor = get()) }
  viewModel { FavouritesViewModel(oppInteractor = get()) }
  viewModel {
    FilterViewModel(
      oppInteractor = get(),
      companyInteractor = get(),
      dataBaseInteractor = get()
    )
  }
}