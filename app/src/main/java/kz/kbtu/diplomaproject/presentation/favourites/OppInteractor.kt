package kz.kbtu.diplomaproject.presentation.favourites

import kz.kbtu.diplomaproject.data.backend.main.opportunity.JobCategory
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.OppService

interface OppInteractor {
  fun addToFav(id: Int, opportunityDTO: OpportunityDTO): Async<DataResult<Boolean?>>
  fun getFavourites(): Async<DataResult<List<OpportunityDTO>?>>
  fun getCategories(): Async<DataResult<List<JobCategory>?>>
  fun filterOpportunity(
    category: Int?,
    type: String?,
    contract: String?,
    company: Int?,
    title: String?
  ): Async<DataResult<List<OpportunityDTO>?>>

}

class OppInteractorImpl(private val oppService: OppService) : OppInteractor, CoroutineInteractor {
  override fun addToFav(id: Int, opportunityDTO: OpportunityDTO) = async {
    oppService.addToFav(id, opportunityDTO)
  }

  override fun getFavourites() = async {
    oppService.getFavourites()
  }

  override fun getCategories() = async {
    oppService.getAllCategories()
  }

  override fun filterOpportunity(
    category: Int?,
    type: String?,
    contract: String?,
    company: Int?,
    title: String?
  ) = async {
    oppService.filterOpportunity(
      title = title,
      category = category,
      type = type,
      contract = contract,
      company = company
    )
  }
}