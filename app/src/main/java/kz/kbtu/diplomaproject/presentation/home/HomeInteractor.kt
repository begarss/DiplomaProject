package kz.kbtu.diplomaproject.presentation.home

import kz.kbtu.diplomaproject.data.backend.banner.BannerDTO
import kz.kbtu.diplomaproject.data.backend.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.HomeService

interface HomeInteractor {
  fun getBanners(): Async<DataResult<List<BannerDTO>?>>
  fun getSubscribedOpports(): Async<DataResult<List<OpportunityDTO>?>>

}

class HomeInteractorImpl(private val homeService: HomeService) : HomeInteractor,
  CoroutineInteractor {
  override fun getBanners() = async {
    homeService.getBanners()
  }

  override fun getSubscribedOpports() = async {
    homeService.getSubscribedOpports()
  }

}