package kz.kbtu.diplomaproject.data.backend.auth

import com.google.gson.annotations.SerializedName

data class VerifyResponse(
  @SerializedName("Token")
  val token: String
)
