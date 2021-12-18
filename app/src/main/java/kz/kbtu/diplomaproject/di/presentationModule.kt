package kz.kbtu.diplomaproject.di

import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
  viewModel { SharedViewModel(userInteractor = get()) }
}