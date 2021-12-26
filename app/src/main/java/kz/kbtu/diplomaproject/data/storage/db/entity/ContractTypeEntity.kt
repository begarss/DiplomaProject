package kz.kbtu.diplomaproject.data.storage.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ContractModel

@Entity(tableName = "contract_type")
data class ContractTypeEntity(
  @PrimaryKey
  val id: Int,
  val name: String
) {
  fun toModel(): ContractModel {
    return ContractModel(id, name)
  }
}

fun List<ContractTypeEntity>.mapToModel(): List<ContractModel> {
  return this.map { it.toModel() }
}
