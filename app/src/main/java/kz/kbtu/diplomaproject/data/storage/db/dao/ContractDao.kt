package kz.kbtu.diplomaproject.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kz.kbtu.diplomaproject.data.storage.db.entity.ContractTypeEntity

@Dao
abstract class ContractDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract fun insertContractTypeList(list: List<ContractTypeEntity>)

  @Query("SELECT * from contract_type")
  abstract fun getAllContractType(): List<ContractTypeEntity>
}