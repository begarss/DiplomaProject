package kz.kbtu.diplomaproject.data.backend.main


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BannerDTO(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    var image: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?
) : Parcelable