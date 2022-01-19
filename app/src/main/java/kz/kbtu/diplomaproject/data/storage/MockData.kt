package kz.kbtu.diplomaproject.data.storage

import kz.kbtu.diplomaproject.presentation.explore.filter.vo.ContractModel
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.JobTypeModel

object MockData {

  fun getMockJobType(): ArrayList<JobTypeModel> {
    val jobTypes = arrayListOf<JobTypeModel>()
    jobTypes.add(JobTypeModel(0, "Vacancy"))
    jobTypes.add(JobTypeModel(1, "Internship"))
    jobTypes.add(JobTypeModel(2, "Conference"))
    jobTypes.add(JobTypeModel(3, "Scholarship"))
    jobTypes.add(JobTypeModel(4, "Hackathon"))
    return jobTypes
  }

  fun getMockContracts(): ArrayList<ContractModel> {
    val contracts = arrayListOf<ContractModel>()
    contracts.add(ContractModel(0, "full-time"))
    contracts.add(ContractModel(1, "part-time"))
    contracts.add(ContractModel(2, "project-time"))
    return contracts
  }
}