package kz.kbtu.diplomaproject.data.backend.auth


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("email")
    val email: String?,
    @SerializedName("message")
    val message: String?
)