package kz.kbtu.diplomaproject.data.backend.auth


import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("token")
    val token: String?
)