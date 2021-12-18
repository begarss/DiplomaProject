package kz.kbtu.diplomaproject.domain.services

import kz.kbtu.diplomaproject.data.backend.banner.HomeApi
import kz.kbtu.diplomaproject.data.backend.banner.BannerDTO
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface HomeService {
  suspend fun getBanners(): DataResult<List<BannerDTO>?>
}

class HomeServiceImpl(private val homeApi: HomeApi) : HomeService {
  override suspend fun getBanners(): DataResult<List<BannerDTO>?> = safeCall {
    val response = homeApi.getBanners()
    val body = response.body()
    body
  }

}