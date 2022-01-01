package kz.kbtu.diplomaproject.data.storage.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_opps")
data class FavouriteOpportunitiesEntity(
  @PrimaryKey
  val id: Int?,
  val title: String?,
  val jobType: String?,
  val description: String?,
  val deadline: String?,
  val isFavourate: Boolean?,
)
