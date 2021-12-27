package kz.kbtu.diplomaproject.presentation.explore.filter.vo

import kz.kbtu.diplomaproject.data.storage.db.entity.ContractTypeEntity
import kz.kbtu.diplomaproject.data.storage.db.entity.JobTypeEntity

data class JobTypeModel(
  val id: Int,
  val name: String
) {
  fun toEntity(): JobTypeEntity {
    return JobTypeEntity(id, name)
  }
}

fun List<JobTypeModel>.mapToEntity(): List<JobTypeEntity> {
  return this.map { it.toEntity() }
}
