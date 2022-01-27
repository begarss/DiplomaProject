package kz.kbtu.diplomaproject.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kz.kbtu.diplomaproject.data.storage.db.entity.FollowedCompanyIdEntity

@Dao
abstract class FollowedCompanyDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertFollowList(list: List<FollowedCompanyIdEntity>)

  @Insert(onConflict = OnConflictStrategy.ABORT)
  abstract fun insertFollowedCompanyId(followedIdEntity: FollowedCompanyIdEntity)

  @Query("SELECT * from followed_company_ids WHERE id=:id")
  abstract fun getFollowedById(id: Int): FollowedCompanyIdEntity?

  @Query("DELETE FROM followed_company_ids WHERE id=:id ")
  abstract fun deleteById(id: Int)

  @Query("SELECT * from followed_company_ids")
  abstract fun getAllFollowed(): List<FollowedCompanyIdEntity>

  @Query("DELETE FROM followed_company_ids")
  abstract fun removeAll()

}