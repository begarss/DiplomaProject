package kz.kbtu.diplomaproject.data.backend.opportunity


import com.google.gson.annotations.SerializedName

data class Company(
    @SerializedName("about_company")
    val aboutCompany: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("picture")
    val picture: String?
)