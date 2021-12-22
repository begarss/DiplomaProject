package kz.kbtu.diplomaproject.data.backend.opportunity


import com.google.gson.annotations.SerializedName

data class OpportunityDTOItem(
    @SerializedName("company")
    val company: Company?,
    @SerializedName("deadline")
    val deadline: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_favourate")
    val isFavourate: Boolean?,
    @SerializedName("job_category")
    val jobCategory: JobCategory?,
    @SerializedName("job_type")
    val jobType: String?,
    @SerializedName("title")
    val title: String?
)