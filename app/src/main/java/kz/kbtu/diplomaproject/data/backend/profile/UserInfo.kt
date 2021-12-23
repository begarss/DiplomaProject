package kz.kbtu.diplomaproject.data.backend.profile

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfo(
  @SerializedName("birthday")
  val birthday: String?,
  @SerializedName("contact_number")
  val contactNumber: String?,
  @SerializedName("email")
  val email: String?,
  @SerializedName("first_name")
  val firstName: String?,
  @SerializedName("id")
  val id: Int?,
  @SerializedName("last_name")
  val lastName: String?,
  @SerializedName("prof_cv")
  val profCv: String?,
  @SerializedName("prof_image")
  var profImage: String?,
  @SerializedName("zipcode")
  val zipcode: String?
) : Parcelable