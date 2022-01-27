package kz.kbtu.diplomaproject.data.storage.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "followed_company_ids")
data class FollowedCompanyIdEntity(
  @PrimaryKey
  val id: Int?
)
