package kz.kbtu.diplomaproject.data.backend.opportunity

import com.google.gson.annotations.SerializedName

data class Company(
  @SerializedName("id")
  val id: Int?,
  @SerializedName("about_company")
  val aboutCompany: String?,
  @SerializedName("name")
  val name: String?,
  @SerializedName("picture")
  val picture: String?,
  @SerializedName("read_more")
  val readMoreLink: String?
)