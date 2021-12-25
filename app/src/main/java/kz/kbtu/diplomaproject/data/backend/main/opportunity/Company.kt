package kz.kbtu.diplomaproject.data.backend.main.opportunity

import com.google.gson.annotations.SerializedName

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
  val readMoreLink: String?
)