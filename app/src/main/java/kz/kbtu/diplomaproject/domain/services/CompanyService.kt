package kz.kbtu.diplomaproject.domain.services

import kz.airba.infrastructure.helpers.setImageUrl
import kz.kbtu.diplomaproject.data.backend.main.CompanyApi
import kz.kbtu.diplomaproject.data.backend.main.opportunity.Company
import kz.kbtu.diplomaproject.data.backend.main.opportunity.OpportunityDTO
import kz.kbtu.diplomaproject.data.storage.db.dao.FavDao
import kz.kbtu.diplomaproject.data.storage.db.dao.FollowedCompanyDao
import kz.kbtu.diplomaproject.data.storage.db.entity.FollowedCompanyIdEntity
import kz.kbtu.diplomaproject.domain.helpers.operators.safeCall
import kz.kbtu.diplomaproject.domain.model.DataResult

interface CompanyService {
  suspend fun getCompanies(): DataResult<List<Company>?>
  suspend fun searchCompany(name: String?): DataResult<List<Company>?>
  suspend fun getCompanyDetail(id: Int): DataResult<Company?>
  suspend fun makeSubscribe(id: Int): DataResult<Boolean?>
  suspend fun getOppByCompany(id: Int): DataResult<List<OpportunityDTO>?>
  suspend fun getSubscribedCompanies(): DataResult<List<Company>?>
}

class CompanyServiceImpl(
  private val companyApi: CompanyApi,
  private val favDao: FavDao,
  private val followedCompanyDao: FollowedCompanyDao
) :
  CompanyService {
  override suspend fun getCompanies(): DataResult<List<Company>?> = safeCall {
    val response = companyApi.getAllCompanies()
    val body = response.body()
    body?.data?.forEach {
      it.picture = "http://ithuntt.pythonanywhere.com/${it.picture}"
    }
    body?.data
  }

  override suspend fun searchCompany(name: String?): DataResult<List<Company>?> = safeCall {
    val response = companyApi.searchCompany(name)
    val body = response.body()
    val followedCompanyIds = followedCompanyDao.getAllFollowed()
    val followedId = followedCompanyIds.map {
      it.id
    }
    body?.forEach {
      if (followedId.contains(it.id)) {
        it.isSubscribed = true
      }
//      setImageUrl(it)
    }
    body
  }

  override suspend fun getCompanyDetail(id: Int): DataResult<Company?> = safeCall {
    val response = companyApi.getCompanyDetail(id)
    val body = response.body()
    body
  }

  override suspend fun makeSubscribe(id: Int): DataResult<Boolean?> = safeCall {
    val response = companyApi.makeSubscribe(id)
    val body = response.body()
    if (body?.status == "success") {
      val localFollowedCompany = followedCompanyDao.getFollowedById(id)
      if (localFollowedCompany == null) {
        followedCompanyDao.insertFollowedCompanyId(FollowedCompanyIdEntity(id))
      } else {
        followedCompanyDao.deleteById(id)
      }
    }
    body?.status == "success"
  }

  override suspend fun getOppByCompany(id: Int): DataResult<List<OpportunityDTO>?> = safeCall {
    val response = companyApi.getOppByCompany(id)
    val body = response.body()
    body?.forEach {
      setImageUrl(it)
    }
    val favs = favDao.getAllFavs()
    val favId = favs.map {
      it.id
    }
    body?.forEach {
      if (favId.contains(it.id)) {
        it.isFavourate = true
      }
    }
    body
  }

  override suspend fun getSubscribedCompanies(): DataResult<List<Company>?> = safeCall {
    val response = companyApi.getSubscribedCompanies()
    val body = response.body()
    if (body?.isSuccessful() == true) {
      followedCompanyDao.removeAll()
      body.data.map {
        FollowedCompanyIdEntity(id = it.id)
      }.let {
        followedCompanyDao.insertFollowList(it)
      }
    }
    body?.data?.forEach {
      it.picture = "http://ithuntt.pythonanywhere.com/${it.picture}"
    }
    body?.data
  }

}