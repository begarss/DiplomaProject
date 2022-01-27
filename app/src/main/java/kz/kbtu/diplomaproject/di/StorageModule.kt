package kz.kbtu.diplomaproject.di

import android.content.Context
import kz.kbtu.diplomaproject.data.storage.AppDatabase
import kz.kbtu.diplomaproject.data.storage.db.dao.ContractDao
import kz.kbtu.diplomaproject.data.storage.db.dao.FavDao
import kz.kbtu.diplomaproject.data.storage.db.dao.FollowedCompanyDao
import kz.kbtu.diplomaproject.data.storage.db.dao.JobTypeDao
import org.koin.dsl.module

val storageModule = module {
  single { provideDatabase(context = get()) }
  single { provideContractDao(appDatabase = get()) }
  single { provideJobTypeDao(appDatabase = get()) }
  single { provideFavDao(appDatabase = get()) }
  single { provideFollowedDao(appDatabase = get()) }
}

fun provideDatabase(context: Context): AppDatabase {
  return AppDatabase.getDatabase(context)
}

fun provideJobTypeDao(appDatabase: AppDatabase): JobTypeDao {
  return appDatabase.getJobTypeDao()
}

fun provideContractDao(appDatabase: AppDatabase): ContractDao {
  return appDatabase.getContractTypeDao()
}

fun provideFavDao(appDatabase: AppDatabase): FavDao {
  return appDatabase.getFavDao()
}

fun provideFollowedDao(appDatabase: AppDatabase): FollowedCompanyDao {
  return appDatabase.getFollowedDao()
}
