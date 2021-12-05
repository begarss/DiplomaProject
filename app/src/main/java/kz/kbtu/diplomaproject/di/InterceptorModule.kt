package kz.kbtu.diplomaproject.di

import org.koin.dsl.module

val interceptorModule = module {
  single { MainInterceptor(get()) }
}