package kz.kbtu.diplomaproject.data.backend.auth


import com.google.gson.annotations.SerializedName

data class RegistrationResponse(
    @SerializedName("email")
    val email: String?,
    @SerializedName("response")
    val response: String?,
    @SerializedName("token")
    val token: String?
)