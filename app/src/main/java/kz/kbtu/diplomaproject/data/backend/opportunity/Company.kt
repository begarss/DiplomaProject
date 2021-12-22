package kz.kbtu.diplomaproject.data.backend.opportunity


import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("name")
    val name: String?,
    @SerializedName("picture")
    val picture: String?
)