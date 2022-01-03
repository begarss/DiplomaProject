package kz.kbtu.diplomaproject.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kz.kbtu.diplomaproject.data.storage.db.dao.ContractDao
import kz.kbtu.diplomaproject.data.storage.db.dao.FavDao
import kz.kbtu.diplomaproject.data.storage.db.dao.JobTypeDao
import kz.kbtu.diplomaproject.data.storage.db.entity.ContractTypeEntity
import kz.kbtu.diplomaproject.data.storage.db.entity.FavouriteOpportunitiesEntity
import kz.kbtu.diplomaproject.data.storage.db.entity.JobTypeEntity

@Database(
  entities = [
    JobTypeEntity::class,
    ContractTypeEntity::class,
    FavouriteOpportunitiesEntity::class
  ],
  version = 2,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  companion object {
    private const val DATABASE_NAME = "it_hunt_app_database.db"

    fun getDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .apply { fallbackToDestructiveMigration() }
        .build()
    }
  }

  abstract fun getJobTypeDao(): JobTypeDao
  abstract fun getContractTypeDao(): ContractDao
  abstract fun getFavDao(): FavDao
}