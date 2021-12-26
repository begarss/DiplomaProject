package kz.kbtu.diplomaproject.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kz.kbtu.diplomaproject.data.storage.db.entity.JobTypeEntity

@Dao
abstract class JobTypeDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertJobTypeList(list: List<JobTypeEntity>)

  @Query("SELECT * from job_type")
  abstract fun getAllJobType(): List<JobTypeEntity>
}