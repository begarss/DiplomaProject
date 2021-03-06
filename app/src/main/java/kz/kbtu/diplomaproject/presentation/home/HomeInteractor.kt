package kz.kbtu.diplomaproject.presentation.home

import kz.kbtu.diplomaproject.data.backend.main.BannerDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.backend.main.opportunity.PostDetail
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.HomeService

interface HomeInteractor {
  fun getBanners(): Async<DataResult<List<BannerDTO>?>>
  fun getSubscribedOpports(): Async<DataResult<List<OpportunityDTO>?>>
  fun getDetails(id: Int): Async<DataResult<PostDetail?>>
  fun getOpportunities(): Async<DataResult<List<OpportunityDTO>?>>

}

class HomeInteractorImpl(private val homeService: HomeService) : HomeInteractor,
  CoroutineInteractor {
  override fun getBanners() = async {
    homeService.getBanners()
  }

  override fun getSubscribedOpports() = async {
    homeService.getSubscribedOpports()
  }

  override fun getDetails(id: Int) = async {
    homeService.getDetails(id)
  }

  override fun getOpportunities() = async {
    homeService.getOpportunities()
  }

}