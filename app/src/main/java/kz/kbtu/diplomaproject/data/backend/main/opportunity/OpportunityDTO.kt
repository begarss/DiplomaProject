package kz.kbtu.diplomaproject.data.backend.main.opportunity

import com.google.gson.annotations.SerializedName

data class OpportunityDTO(
  @SerializedName("id")
  val id: Int?,
  @SerializedName("title")
  val title: String?,
  @SerializedName("job_type")
  val jobType: String?,
  @SerializedName("description")
  val description: String?,
  @SerializedName("company")
  val company: Company?,
  @SerializedName("deadline")
  val deadline: String?,
  @SerializedName("is_favourate")
  val isFavourate: Boolean?,
  @SerializedName("job_category")
  val jobCategory: JobCategory?,
)