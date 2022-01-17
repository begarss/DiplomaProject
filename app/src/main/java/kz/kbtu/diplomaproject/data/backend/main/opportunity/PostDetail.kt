package kz.kbtu.diplomaproject.data.backend.main.opportunity


import com.google.gson.annotations.SerializedName

data class PostDetail(
    @SerializedName("apply_link")
    val applyLink: String?,
    @SerializedName("company")
    val company: Company?,
    @SerializedName("contract_type")
    val contractType: String?,
    @SerializedName("deadline")
    val deadline: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_favourate")
    var isFavourate: Boolean?,
    @SerializedName("job_category")
    val jobCategory: JobCategory?,
    @SerializedName("job_type")
    val jobType: String?,
    @SerializedName("key_benefits")
    val keyBenefits: String?,
    @SerializedName("location")
    val location: String?,
    @SerializedName("read_more_link")
    val readMoreLink: String?,
    @SerializedName("requirements")
    val requirements: String?,
    @SerializedName("title")
    val title: String?
)