package kz.kbtu.diplomaproject.data.storage.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.JobTypeModel

@Entity(tableName = "job_type")
data class JobTypeEntity(
  @PrimaryKey
  val id: Int,
  val name: String
) {
  fun toModel(): JobTypeModel {
    return JobTypeModel(id, name)
  }
}

fun List<JobTypeEntity>.mapToModel(): List<JobTypeModel> {
  return this.map { it.toModel() }
}
