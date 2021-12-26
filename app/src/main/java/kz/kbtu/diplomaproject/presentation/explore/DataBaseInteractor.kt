package kz.kbtu.diplomaproject.presentation.explore

import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.services.DataBaseService
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ContractModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.JobTypeModel

interface DataBaseInteractor {
  fun getJobTypes(): Async<List<JobTypeModel>>
  fun getContractTypes(): Async<List<ContractModel>>
  fun insertJobTypes(list: List<JobTypeModel>): Async<Unit>
  fun insertContractTypes(list: List<ContractModel>): Async<Unit>
}

class DataBaseInteractorImpl(private val dataBaseService: DataBaseService) : DataBaseInteractor,
  CoroutineInteractor {

  override fun getJobTypes() = async {
    dataBaseService.getJobTypes()
  }

  override fun getContractTypes() = async { dataBaseService.getContractTypes() }

  override fun insertJobTypes(list: List<JobTypeModel>) = async {
    dataBaseService.insertJobTypes(list)
  }

  override fun insertContractTypes(list: List<ContractModel>) = async {
    dataBaseService.insertContractTypes(list)
  }

}