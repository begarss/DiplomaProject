package kz.kbtu.diplomaproject.data.backend.banner


import com.google.gson.annotations.SerializedName

data class BannerDTO(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?
)