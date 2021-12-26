package kz.kbtu.diplomaproject.presentation.explore.filter.vo

import kz.kbtu.diplomaproject.data.storage.db.entity.ContractTypeEntity

data class ContractModel(
  val id: Int,
  val name: String
) {
  fun toEntity(): ContractTypeEntity {
    return ContractTypeEntity(id, name)
  }
}

fun List<ContractModel>.mapToEntity(): List<ContractTypeEntity> {
  return this.map { it.toEntity() }
}
