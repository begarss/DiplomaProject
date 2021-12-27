package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.storage.db.dao.ContractDao
import kz.kbtu.diplomaproject.data.storage.db.dao.JobTypeDao
import kz.kbtu.diplomaproject.data.storage.db.entity.mapToModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ContractModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.JobTypeModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.mapToEntity

interface DataBaseService {
  suspend fun getJobTypes(): List<JobTypeModel>
  suspend fun getContractTypes(): List<ContractModel>
  suspend fun insertJobTypes(list: List<JobTypeModel>)
  suspend fun insertContractTypes(list: List<ContractModel>)
}

class DataBaseServiceImpl(
  private val jobTypeDao: JobTypeDao,
  private val contractDao: ContractDao
) : DataBaseService {

  override suspend fun getJobTypes() = jobTypeDao.getAllJobType().mapToModel()

  override suspend fun getContractTypes() = contractDao.getAllContractType().mapToModel()

  override suspend fun insertJobTypes(list: List<JobTypeModel>) {
    jobTypeDao.insertJobTypeList(list.mapToEntity())
  }

  override suspend fun insertContractTypes(list: List<ContractModel>) {
    contractDao.insertContractTypeList(list.mapToEntity())
  }

}