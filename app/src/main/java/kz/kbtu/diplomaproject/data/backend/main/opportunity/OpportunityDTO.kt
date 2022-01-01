package kz.kbtu.diplomaproject.data.backend.main.opportunity

import com.google.gson.annotations.SerializedName
import kz.kbtu.diplomaproject.data.storage.db.entity.FavouriteOpportunitiesEntity

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
  var isFavourate: Boolean?,
  @SerializedName("job_category")
  val jobCategory: JobCategory?,
) {
  fun toEntity(): FavouriteOpportunitiesEntity {
    return FavouriteOpportunitiesEntity(
      id = id,
      title = title,
      jobType = jobType,
      description = description,
      deadline = deadline,
      isFavourate = isFavourate
    )
  }
}

fun List<OpportunityDTO>.mapToEntity(): List<FavouriteOpportunitiesEntity> {
  return this.map { it.toEntity() }
}