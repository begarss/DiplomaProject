package kz.kbtu.diplomaproject.data.backend.profile


import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("birthday")
    val birthday: Any?,
    @SerializedName("contact_number")
    val contactNumber: Any?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("first_name")
    val firstName: Any?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_name")
    val lastName: Any?,
    @SerializedName("prof_cv")
    val profCv: Any?,
    @SerializedName("prof_image")
    val profImage: Any?,
    @SerializedName("zipcode")
    val zipcode: Any?
)