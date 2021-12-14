package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.auth.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { LoginViewModel() }
}