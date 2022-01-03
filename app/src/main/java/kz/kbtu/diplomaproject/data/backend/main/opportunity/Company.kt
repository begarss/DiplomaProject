package kz.kbtu.diplomaproject.data.backend.main.opportunity

import com.google.gson.annotations.SerializedName
import kz.kbtu.diplomaproject.presentation.explore.filter.vo.CompanyModel

data class Company(
  @SerializedName("id")
  val id: Int?,
  @SerializedName("about_company")
  val aboutCompany: String?,
  @SerializedName("name")
  val name: String?,
  @SerializedName("picture")
  var picture: String?,
  @SerializedName("read_more")
  val readMoreLink: String?,
  @SerializedName("is_subscribed")
  val isSubscribed: Boolean?
) {
  fun toModel(): CompanyModel {
    return CompanyModel(id, name)
  }
}

fun List<Company>.mapToModel(): List<CompanyModel> {
  return this.map { it.toModel() }
}