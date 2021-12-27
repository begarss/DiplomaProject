package kz.kbtu.diplomaproject.data.backend.main.opportunity


import com.google.gson.annotations.SerializedName

data class JobCategory(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?
)