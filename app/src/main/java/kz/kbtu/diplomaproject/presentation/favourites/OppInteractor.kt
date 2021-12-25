package kz.kbtu.diplomaproject.presentation.favourites

import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.Async
import kz.kbtu.diplomaproject.domain.helpers.operators.CoroutineInteractor
import kz.kbtu.diplomaproject.domain.model.DataResult
import kz.kbtu.diplomaproject.domain.services.OppService

interface OppInteractor {
  fun addToFav(id: Int): Async<DataResult<Boolean?>>
  fun getFavourites(): Async<DataResult<List<OpportunityDTO>?>>
}

class OppInteractorImpl(private val oppService: OppService) : OppInteractor, CoroutineInteractor {
  override fun addToFav(id: Int) = async {
    oppService.addToFav(id)
  }

  override fun getFavourites() = async {
    oppService.getFavourites()
  }
}