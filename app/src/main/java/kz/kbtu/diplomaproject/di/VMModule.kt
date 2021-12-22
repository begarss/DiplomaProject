package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.AuthViewModel
import kz.kbtu.diplomaproject.presentation.home.HomeViewModel
import kz.kbtu.diplomaproject.presentation.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { AuthViewModel(authInteractor = get()) }
  viewModel { HomeViewModel(homeInteractor = get()) }
  viewModel { ProfileViewModel(profileInteractor = get()) }
}