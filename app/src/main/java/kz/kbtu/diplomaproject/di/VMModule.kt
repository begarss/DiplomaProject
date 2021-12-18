package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { AuthViewModel(authInteractor = get()) }
}