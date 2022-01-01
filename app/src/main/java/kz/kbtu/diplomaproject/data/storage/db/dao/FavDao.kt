package kz.kbtu.diplomaproject.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kz.kbtu.diplomaproject.data.storage.db.entity.FavouriteOpportunitiesEntity

@Dao
abstract class FavDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertFavList(list: List<FavouriteOpportunitiesEntity>)

  @Insert(onConflict = OnConflictStrategy.ABORT)
  abstract fun insertFav(favouriteOpportunitiesEntity: FavouriteOpportunitiesEntity)

  @Query("SELECT * from fav_opps WHERE id=:id")
  abstract fun getFavById(id: Int): FavouriteOpportunitiesEntity?

  @Query("DELETE FROM fav_opps WHERE id=:id ")
  abstract fun deleteById(id: Int)

  @Query("SELECT * from fav_opps")
  abstract fun getAllFavs(): List<FavouriteOpportunitiesEntity>

  @Query("DELETE FROM fav_opps")
  abstract fun removeAll()
}