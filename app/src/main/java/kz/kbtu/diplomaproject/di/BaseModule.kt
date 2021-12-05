package kz.kbtu.diplomaproject.di

import kotlinx.coroutines.Dispatchers
import kz.kbtu.diplomaproject.data.storage.Preferences
import kz.kbtu.diplomaproject.data.storage.PreferencesImpl
import kz.kbtu.diplomaproject.presentation.explore.SharedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val baseModule = module {
  single<Preferences> { PreferencesImpl(get(), get()) }
  single { Dispatchers.IO }
  viewModel { SharedViewModel() }
}

